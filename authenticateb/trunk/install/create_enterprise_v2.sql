create or replace procedure check_enterprise_v2(
       p_result out number,
	   p_short_desc out varchar2,
	   p_tenant_id IN FBI_ENTERPRISE.TENANTID%TYPE,
	   p_tenant_realm IN FBI_ENTERPRISE.TENANT_REALM%TYPE,
	   p_tenant_name IN FBI_ENTERPRISE.TENANT_NAME%TYPE,
	   p_auto_onboarding in FBI_ENTERPRISE.AUTO_ONBOARDING%TYPE,
	   p_max_user_ids in FBI_ENTERPRISE.MAX_USERS_ID%TYPE,
	   p_provider IN FBI_FED_TYPE.FED_TYPE%TYPE,
	   p_check_provider IN FBI_FED_TYPE.FED_TYPE%TYPE,
	   p_contact_name IN FBI_ENTERPRISE_CONTACT.CONTACT_NAME%TYPE,
	   p_contact_email IN FBI_ENTERPRISE_CONTACT.CONTACT_EMAIL%TYPE,
	   p_contact_phone IN FBI_ENTERPRISE_CONTACT.CONTACT_PHONE%TYPE,
	   p_contact_type IN FBI_ENTERPRISE_CONTACT.CONTACT_TYPE%TYPE,
	   p_create_tenant_uri IN FBI_FED_TYPE.FED_TYPE%TYPE,
	   p_uri_type in FBI_URI_TYPE.URI_TYPE%TYPE,
	   p_uri in FBI_ENTERPRISE_RESOURCE_URI.URI%TYPE,
	   p_created_by in FBI_ENTERPRISE.created_by%TYPE)
IS
   v_exist_provider number;
   v_exist_tenant_id number;
   v_uri_type_id number;
   v_exists_uri_type number;
   v_fed_type_id number;
BEGIN
 
  if (p_check_provider ='Y')
  then
		select count(*) into v_exist_provider 
		from fbi_fed_type
		where upper(fed_type)=upper(p_provider);
    
		if (v_exist_provider != 0)
		then
				select FED_TYPE_ID into v_fed_type_id 
				from fbi_fed_type 
				where  upper(fed_type)=upper(p_provider);
		else 
				 p_result := -2000;
				 p_short_desc := 'Provider not valid ' || p_provider;
				 GOTO end_end;
		end if;
  end if;		
  
  SELECT COUNT(*) INTO v_exist_tenant_id 
         FROM FBI_ENTERPRISE 
  WHERE TENANTID=p_tenant_id;
  
  IF v_exist_tenant_id = 0 THEN
    INSERT INTO FBI_ENTERPRISE (TENANTID, TENANT_REALM, TENANT_NAME,AUTO_ONBOARDING,MAX_USERS_ID,created_by)
    VALUES (p_tenant_id, p_tenant_realm,p_tenant_name,p_auto_onboarding,p_max_user_ids,p_created_by);
  ELSE
	 p_result := -2001;
	 p_short_desc :='Tenant Id already existing';
   GOTO end_end;
  END IF;
  
  if (p_check_provider ='Y')
  then
		insert into FBI_TENANT_FED_TYPE (FED_TYPE_ID,TENANTID)
		values (v_fed_type_id,p_tenant_id);
		
		if (p_create_tenant_uri='Y')
    then
			SELECT count(*) INTO v_exists_uri_type
			FROM FBI_URI_TYPE 
			WHERE upper(URI_TYPE)=upper(p_uri_type);
			
			if (v_exists_uri_type=1) then
				  SELECT URI_TYPE_ID INTO v_uri_type_id 
						 FROM FBI_URI_TYPE 
				  WHERE upper(URI_TYPE)=upper(p_uri_type);
                  insert into FBI_ENTERPRISE_RESOURCE_URI (FED_TYPE_ID,URI_TYPE_ID,URI,CREATED_BY,TENANTID)
                  values (v_fed_type_id,v_uri_type_id,p_uri,p_created_by,p_tenant_id);
			else 
				p_result := -2002;
				p_short_desc := 'DB Config error in table FBI_URI_TYPE. Missing type: ' ||  p_uri_type;
				GOTO end_end;
			end if;
      
		end if;
  end if;		
  		
 insert into FBI_ENTERPRISE_CONTACT(TENANTID,CONTACT_NAME,CONTACT_EMAIL,CONTACT_PHONE,CONTACT_TYPE,CREATED_BY) 
 values (p_tenant_id,p_contact_name,p_contact_email,p_contact_phone,p_contact_type,p_created_by);
   
  p_result := 0;
  p_short_desc := 'Success';
  GOTO end_end;
  <<end_end>>
  NULL;
END;
  