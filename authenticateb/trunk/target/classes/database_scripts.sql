CREATE OR REPLACE PROCEDURE fbi_stg_tenant_user_chk (
   p_user_id      IN       fbi_federated_userid.iam_userid%TYPE,
   p_tenant_id    IN       fbi_enterprise.tenantid%TYPE,
   p_email        IN       fbi_federated_userid.fed_email%TYPE,
   p_created_by   IN       fbi_federated_userid.created_by%TYPE,
   p_message      OUT      VARCHAR2
)
IS
   v_tenantid            fbi_enterprise.tenantid%TYPE;
   v_userid              fbi_federated_userid.iam_userid%TYPE;
   v_userid_count        NUMBER;
   v_tenant_count        NUMBER;
   user_already_exists   EXCEPTION;
   tenant_not_found      EXCEPTION;
   maxuid_exceeded       EXCEPTION;
   v_count               NUMBER;
   v_status              fbi_federated_userid.status%TYPE;
   v_maxuid              fbi_enterprise.max_user_ids%TYPE;
   v_row_count           number;
BEGIN
--Checking User Exists or not
   SELECT COUNT (1)
     INTO v_userid_count
     FROM fbi_federated_userid
    WHERE UPPER (iam_userid) = UPPER (p_user_id);

   IF nvl(v_userid_count,0) > 0
   THEN
      RAISE user_already_exists;
   END IF;

--CHECKING Tenant Exists Or Not
   SELECT COUNT (1)
     INTO v_tenant_count
     FROM fbi_enterprise
    WHERE UPPER (tenantid) = UPPER (p_tenant_id);

   IF nvl(v_count,0) <> 1
   THEN
      RAISE tenant_not_found;
   END IF;

--Updating status to LOCKED
   BEGIN
      SELECT status
        INTO v_status
        FROM fbi_federated_userid
       WHERE iam_userid = p_user_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;
--Updating status to Locked
   
   IF v_status is null or upper(v_status) <>'LOCKED'
THEN
UPDATE FBI_FEDERATED_USERID
set status='LOCKED'
where IAM_USERID=P_USER_ID;
END IF;
   

   BEGIN
      SELECT DISTINCT max_user_ids
                 INTO v_maxuid
                 FROM fbi_enterprise
                WHERE tenantid = p_tenant_id AND ROWNUM = 1;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;

   SELECT COUNT (1)
     INTO v_row_count
     FROM fbi_enterprise;

   IF NVL (v_maxuid,0) > nvl(v_row_count,0)
   THEN
      RAISE maxuid_exceeded;
   END IF;

--Creating federated id
   BEGIN
      INSERT INTO fbi_federated_userid
                  (fed_email, iam_userid, tenantid, created_by
                  )
           VALUES (p_email, p_user_id, p_tenant_id, p_created_by
                  );
   END;
   
   p_message :='SUCCESS';
   
EXCEPTION
   WHEN user_already_exists
   THEN
      p_message := 'The User Entered Already Exists In System';
   WHEN tenant_not_found
   THEN
      p_message := 'The Tenant Id Not Found In System';
   WHEN maxuid_exceeded
   THEN
      p_message :=
         'Number of rows in FBI_ENTERPRISE table exceeding value mentioned in maxuid column';
   WHEN OTHERS
   THEN
      p_message := 'The error is :' || SUBSTR (SQLERRM, 1, 150);
END;


create or replace PROCEDURE fbi_stg_user_fed_id (
   p_user_id      IN       fbi_federated_userid.iam_userid%TYPE,
    p_email        IN       fbi_federated_userid.fed_email%TYPE,
     p_tenant_id    IN       fbi_enterprise.tenantid%TYPE,
     --v_user_federated_id  OUT fbi_fed_partner_userid.fed_partner_userid%Type,
      p_message      OUT      VARCHAR2)
      IS
      v_fed_empid       fbi_fed_partner_userid.fed_empid%Type;
       v_userid_count        NUMBER;
      v_tenant_count        NUMBER;
      
      user_not_exists       EXCEPTION;
      tenant_not_found      EXCEPTION;
      maxuid_exceeded       EXCEPTION;
      BEGIN

  --Checking User Exists or not
  SELECT COUNT (1)
  INTO v_userid_count
  FROM FBI_FEDERATED_USERID
  WHERE (UPPER (IAM_USERID) = UPPER (p_user_id) OR UPPER (FED_EMAIL) = UPPER (p_email));

   IF nvl(v_userid_count,0) <> 1   THEN      RAISE user_not_exists;   END IF;

   --CHECKING Tenant Exists Or Not
    SELECT COUNT (1)
    INTO v_tenant_count
     FROM FBI_FEDERATED_USERID
     WHERE UPPER (TENANTID) = UPPER (p_tenant_id);   IF nvl(v_tenant_count,0) <> 1
     THEN      RAISE tenant_not_found;   END IF;

     SELECT FED_EMP_ID
  INTO v_fed_empid
  FROM FBI_FEDERATED_USERID
  WHERE (UPPER (IAM_USERID) = UPPER (p_user_id) OR UPPER (FED_EMAIL) = UPPER (p_email));
  
  --GETTING USER FEDERATED ID
     SELECT FED_PARTNER_USERID
     INTO p_message
     FROM FBI_FED_PARTNER_USERID
     WHERE FED_EMPID = v_fed_empid;

     EXCEPTION
     WHEN user_not_exists   THEN      p_message := 'USER';
     WHEN tenant_not_found   THEN      p_message := 'TENANT';
     WHEN OTHERS   THEN      p_message := 'The error is :' || SUBSTR (SQLERRM, 1, 150);

     END;



     create or replace PROCEDURE FBI_QUERY_TENANT_REALM (
   p_user_id      IN       fbi_federated_userid.iam_userid%TYPE,
   p_email        IN       fbi_federated_userid.fed_email%TYPE,
   p_tenant_realm	 out             FBI_ENTERPRISE.TENANT_REALM%TYPE,
   p_message      OUT      VARCHAR2
)
IS
   v_userid              fbi_federated_userid.iam_userid%TYPE;
   v_userid_count        NUMBER;
   v_tenant_id           FBI_ENTERPRISE.TENANTID%TYPE;
   tenant_realm_not_exists   EXCEPTION;
BEGIN
   --Checking User Exists or not
   SELECT COUNT (1)
     INTO v_userid_count
     FROM fbi_federated_userid
    WHERE UPPER (iam_userid) = UPPER (p_user_id) or  UPPER (fed_email) = UPPER (p_email);
   IF nvl(v_userid_count,0) = 0
   THEN
      RAISE tenant_realm_not_exists;
   END IF;
   
   

   BEGIN
   SELECT TENANTID     INTO v_tenant_id     FROM fbi_federated_userid
    WHERE UPPER (iam_userid) = UPPER (p_user_id) or  UPPER (fed_email) = UPPER (p_email);
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;

   BEGIN
   SELECT TENANT_REALM     INTO p_tenant_realm     FROM FBI_ENTERPRISE
    WHERE TENANTID = v_tenant_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;

    p_message :='SUCCESS';
   
EXCEPTION
   WHEN tenant_realm_not_exists
   THEN
      p_message := 'The User Entered Not Exists TENANT_REALM In the System';
   
   WHEN OTHERS
   THEN
      p_message := 'The error is :' || SUBSTR (SQLERRM, 1, 150);
END;