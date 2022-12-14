package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @InjectMocks
    VetController controller;

    @Mock
    ClinicService clinicService;

    @Mock
    Map<String, Object> model;

    List<Vet> vets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Vet vet1 = new Vet();
//        vet1.setId(1);
//        vet1.setLastName("Thomas");
//        vet1.setFirstName("Basset");
//        Specialty specialty = new Specialty();
//        specialty.setId(1);
//        specialty.setName("Veto");
//        vet1.getSpecialties().add(specialty);
        vets.add(vet1);

        //GIVEN
        given(clinicService.findVets()).willReturn(vets);
    }

    @Test
    void testShowVetList() {

        //WHEN
        String view = controller.showVetList(model);

        //THEN
        assertThat("vets/vetList").isEqualToIgnoringCase(view);
        then(clinicService).should().findVets();
        then(model).should().put(anyString(), any());
        then(clinicService).shouldHaveNoMoreInteractions();
    }

    @Test
    void testShowResourcesVetList() {
        //WHEN
        Vets vets = controller.showResourcesVetList();

        //THEN
        assertThat(vets.getVetList()).hasSize(1);
        then(clinicService).should().findVets();
        then(clinicService).shouldHaveNoMoreInteractions();
    }
}
