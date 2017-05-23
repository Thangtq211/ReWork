package com.tmobile.federation.services;

import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.federation.dao.IAMUserDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ThangTQ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
public class GetFedIdServiceTest {

    private MockMvc mockMvc;

    @InjectMocks private CreateFedIdservice createFedIdservice;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
          .standaloneSetup(createFedIdservice)
          .build();
    }

    @Test
    public void mockQueryAnExistedFederatedIDFromWebLogicThen200OK() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com&tenantId=xyz123")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value() + " " + HttpStatus.OK.getReasonPhrase()))
          .andExpect(jsonPath("$.object.role").value("SuperAdmin"));
        resultActions.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void mockQueryMissingEmailAnExistedFederatedIDThen200OK() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&tenantId=xyz123")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value() + " " + HttpStatus.OK.getReasonPhrase()))
          .andExpect(jsonPath("$.object.role").value("SuperAdmin"));
        resultActions.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void mockQueryMissingTenantIDAnExistedFederatedIDThen200OK() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value() + " " + HttpStatus.OK.getReasonPhrase()))
          .andExpect(jsonPath("$.object.role").value("SuperAdmin"));
        resultActions.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void mockQueryMissingEmailAndTenantIDAnExistedFederatedIDThen200OK() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value() + " " + HttpStatus.OK.getReasonPhrase()))
          .andExpect(jsonPath("$.object.role").value("SuperAdmin"));
        resultActions.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void mockQueryANonExistedFederatedIDThen400BadRequest() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9&email=emai@mobile.com&tenantId=xyz23")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase()))
          .andExpect(jsonPath("$.object").value("User Does Not Exists"));
    }

    @Test
    public void clientQueryAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com&tenantId=xyz123";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingIAMUserIDAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?email=email@mobile.com&tenantId=xyz123";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingIAMUserIDAndTenantIDAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?email=email@mobile.com";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingEmailAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&tenantId=xyz123";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingTenantAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingEmailAndTenantAnExistedFederatedIDFromWebLogicThen200OK() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("role"));
        assertThat(responseBody, containsString("SuperAdmin"));
    }

    @Test
    public void clientQueryMissingAllFederatedIDFromWebLogicThen400BadRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("400 Bad Request"));
        assertThat(responseBody, containsString("Missing mandatory parameter"));
    }

    @Test
    public void clientQueryANonExistedFederatedIDFromWebLogicThen400BadRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9&email=email1@mobile.com&tenantId=xyz1231";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("400 Bad Request"));
        assertThat(responseBody, containsString("User Does Not Exists"));
    }

    @Test
    public void clientQueryMissAuthorizationHeaderFederatedIDFromWebLogicThen400BadRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9&email=email1@mobile.com&tenantId=xyz1231";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("401 Unauthorized"));
    }

    @Test
    public void clientQueryMissHeaderFederatedIDFromWebLogicThen400BadRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9&email=email1@mobile.com&tenantId=xyz1231";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody, containsString("401 Unauthorized"));
    }

}
