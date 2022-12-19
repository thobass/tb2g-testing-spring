package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml","classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Autowired
    OwnerController controller;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void processCreationForm_ownerIsValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName","Thomas")
                .param("lastName","Thomas")
                .param("address","70 Point De Beauvais")
                .param("city","Bourg le Roi")
                .param("telephone","0635284672"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    void processCreationForm_ownerIsInvalid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("firstName","Thomas")
                        .param("lastName","Thomas")
                        .param("city","Bourg le Roi"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void processFindForm_returnSingleOwner() throws Exception {
        Owner owner = new Owner();
        String name = "Basset";
        owner.setLastName(name);
        owner.setId(1);

        given(clinicService.findOwnerByLastName(name)).willReturn(Lists.newArrayList(owner));
        mockMvc.perform(get("/owners")
                    .param("lastName", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        then(clinicService).should().findOwnerByLastName(anyString());
    }

    @Test
    void processFindForm_returnListOfOwners() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));

        then(clinicService).should().findOwnerByLastName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("");
    }

    @Test
    void processFindForm_ownerNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                        .param("lastName","DontFindMe"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }
}
