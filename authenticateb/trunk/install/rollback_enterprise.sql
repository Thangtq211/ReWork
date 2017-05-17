create or replace procedure rollback_enterprise(
  p_result out number,
  p_short_desc out varchar2,
  p_tenant_id IN FBI_ENTERPRISE.TENANTID%TYPE
)
IS
BEGIN
    delete from FBI_ENTERPRISE_CONTACT where TENANTID=p_tenant_id;
	delete from FBI_TENANT_FED_TYPE where TENANTID=p_tenant_id;
	delete from FBI_ENTERPRISE_RESOURCE_URI where TENANTID=p_tenant_id;
    delete from FBI_ENTERPRISE where TENANTID=p_tenant_id;
	p_result := 0;
	p_short_desc := 'Succes';
	EXCEPTION  
	WHEN OTHERS THEN  
    p_result := -5000;
	p_short_desc := 'DB internal error';
END;