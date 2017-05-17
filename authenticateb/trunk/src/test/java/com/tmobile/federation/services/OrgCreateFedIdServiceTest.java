package com.tmobile.federation.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.enums.StatusEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;






  import static org.mockito.Mockito.mock;
  import static org.mockito.Mockito.when;
  import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

  import javax.servlet.http.HttpServletRequest;

  import org.junit.Before;
  import org.junit.Test;
  import org.mockito.InjectMocks;
  import org.mockito.Mock;
  import org.mockito.MockitoAnnotations;
  import org.springframework.mock.web.MockHttpServletRequest;
  import org.springframework.test.web.servlet.MockMvc;
  import org.springframework.test.web.servlet.setup.MockMvcBuilders;

  import com.fasterxml.jackson.databind.ObjectMapper;
  import com.tmobile.federation.beans.req.FedIdRequestBean;
  import com.tmobile.federation.enums.StatusEnum;
  import com.tmobile.federation.services.CreateFedIdservice;

/**
 *
 * @author Admin
 *
 */

public class OrgCreateFedIdServiceTest {

    private MockMvc mockMvc;

    @Mock
    private MockHttpServletRequest request;

    @InjectMocks
    private CreateFedIdservice fedIdService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
          .standaloneSetup(fedIdService)
          .build();
    }

    @Test
    public void testInvalidHeader() throws Exception{
        FedIdRequestBean requestBean = new FedIdRequestBean();
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    @Test
    public void testMissingEmail() throws Exception{
        request.addHeader("authorization", "admin:12312");
        FedIdRequestBean requestBean = new FedIdRequestBean();
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    @Test
    public void testMissingTenant() throws Exception{
        FedIdRequestBean requestBean = new FedIdRequestBean();
        requestBean.setEmail("test@yopmail.com");
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    @Test
    public void testMissingStatus() throws Exception{
        FedIdRequestBean requestBean = new FedIdRequestBean();
        requestBean.setEmail("test@yopmail.com");
        requestBean.setTenantId("131232131123");
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    @Test
    public void testMissingRole() throws Exception{
        FedIdRequestBean requestBean = new FedIdRequestBean();
        requestBean.setEmail("test@yopmail.com");
        requestBean.setTenantId("131232131123");
        requestBean.setStatus(StatusEnum.ACTIVE.getStatus());
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    @Test
    public void testValidData() throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("authorization")).thenReturn("admin:123123");
        FedIdRequestBean requestBean = new FedIdRequestBean();
        requestBean.setEmail("test@yopmail.com");
        requestBean.setTenantId("131232131123");
        requestBean.setStatus(StatusEnum.ACTIVE.getStatus());
        requestBean.setRole("Admin");
        mockMvc.perform(post("/provisioning/v1/fedId")
          .contentType("application/json")
          .content(asJsonString(requestBean)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value("401 Unauthorized"));
    }

    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

