package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @InjectMocks
    ClinicServiceImpl service;

    @Mock
    PetRepository petRepository;
    @Mock
    VetRepository vetRepository;
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    VisitRepository visitRepository;

    @Test
    void findPetTypes_shouldSucceed() {
        //GIVEN
        List<PetType> petTypes = new ArrayList<>();
        PetType petType = new PetType();
        petType.setName("Berger");
        petTypes.add(petType);
        given(petRepository.findPetTypes()).willReturn(petTypes);

        //WHEN
        Collection<PetType> foundPetTypes = service.findPetTypes();

        //THEN
        assertThat(foundPetTypes).hasSize(1);
        then(petRepository).should().findPetTypes();
        then(petRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void findPetTypes_shouldThrowException() {
        //GIVEN
        given(petRepository.findPetTypes()).willThrow(new DataAccessException("Boom") {});

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> service.findPetTypes());
        then(petRepository).should().findPetTypes();
        then(petRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void findOwnerById_shouldSucceed() {
        //GIVEN
        Owner owner = new Owner();
        owner.setId(1);
        owner.setAddress("70 rue du point de beauvais");
        owner.setCity("Bourg Le Roi");
        owner.setFirstName("Thomas");
        owner.setLastName("Basset");
        given(ownerRepository.findById(anyInt())).willReturn(owner);

        //WHEN
        Owner findedOwner = service.findOwnerById(1);

        //THEN
        assertThat(findedOwner).isNotNull();
        assertThat(findedOwner).isEqualTo(owner);
        then(ownerRepository).should().findById(anyInt());
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findOwnerById_shouldThrowException() {
        //GIVEN
        given(ownerRepository.findById(anyInt())).willThrow(new DataAccessException("Boom") {});

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> service.findOwnerById(1));
        then(ownerRepository).should().findById(anyInt());
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findOwnerByLastName_shouldSucceed() {
        //GIVEN
        List<Owner> owners = new ArrayList<>();
        Owner owner = new Owner();
        owner.setId(1);
        owner.setAddress("70 rue du point de beauvais");
        owner.setCity("Bourg Le Roi");
        owner.setFirstName("Thomas");
        owner.setLastName("Basset");
        owners.add(owner);
        given(ownerRepository.findByLastName(anyString())).willReturn(owners);

        //WHEN
        Collection<Owner> findedOwners = service.findOwnerByLastName("Basset");

        //THEN
        assertThat(findedOwners).hasSize(1);
        then(ownerRepository).should().findByLastName(anyString());
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findOwnerByLastName_shouldThrowException() {
        //GIVEN
        given(ownerRepository.findByLastName(anyString())).willThrow(new DataAccessException("Boom") {});

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> service.findOwnerByLastName("Basset"));
        then(ownerRepository).should().findByLastName(anyString());
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void saveOwner_succeed() {
        //GIVEN
        Owner owner = new Owner();
        doNothing().when(ownerRepository).save(any(Owner.class));

        //WHEN
        service.saveOwner(owner);

        //THEN
        then(ownerRepository).should().save(any(Owner.class));
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void saveOwner_sendException() {
        //GIVEN
        Owner owner = new Owner();
        doThrow(new DataAccessException("Boom") {}).when(ownerRepository).save(any(Owner.class));

        //THEN
        assertThrows(DataAccessException.class, () -> {
            service.saveOwner(owner);
        });
        then(ownerRepository).should().save(any(Owner.class));
        then(ownerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void saveVisit_succeed() {
        //GIVEN
        Visit visit = new Visit();
        doNothing().when(visitRepository).save(any(Visit.class));

        //WHEN
        service.saveVisit(visit);

        //THEN
        then(visitRepository).should().save(any(Visit.class));
        then(visitRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void saveVisit_sendException() {
        //GIVEN
        Visit visit = new Visit();

        doThrow(new DataAccessException("Boom") {}).when(visitRepository).save(any(Visit.class));

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> {
            service.saveVisit(visit);
        });
        then(visitRepository).should().save(any(Visit.class));
        then(visitRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findPetById_shouldSucceed() {
        //GIVEN
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Olly");
        pet.setBirthDate(LocalDate.of(2018,11,20));
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Berger");
        pet.setType(petType);

        given(petRepository.findById(anyInt())).willReturn(pet);

        //WHEN
        Pet findedPet = service.findPetById(1);

        //THEN
        assertThat(findedPet).isNotNull();
        assertThat(findedPet).isEqualTo(pet);
        then(petRepository).should().findById(anyInt());
        then(petRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findPetById_shouldSendException() {
        //GIVEN
        given(petRepository.findById(anyInt())).willThrow(new DataAccessException("Boom") {});

        //THEN
        assertThrows(DataAccessException.class, () -> service.findPetById(1));
        then(petRepository).should().findById(anyInt());
        then(petRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void savePet_succeed() {
        //GIVEN
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Olly");
        pet.setBirthDate(LocalDate.of(2018,11,20));
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Berger");
        pet.setType(petType);
        doNothing().when(petRepository).save(any(Pet.class));

        //WHEN
        service.savePet(pet);

        //THEN
        then(petRepository).should().save(any(Pet.class));
        then(petRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void savePet_sendException() {
        //GIVEN
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Olly");
        pet.setBirthDate(LocalDate.of(2018,11,20));
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Berger");
        pet.setType(petType);

        doThrow(new DataAccessException("Boom") {}).when(petRepository).save(any(Pet.class));

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> {
            service.savePet(pet);
        });
        then(petRepository).should().save(any(Pet.class));
        then(petRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findVets_shouldSucceed() {
        //GIVEN
        List<Vet> vets = new ArrayList<>();
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("BASSET");
        vet.setLastName("Thomas");
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Veto");
        vet.addSpecialty(specialty);
        vets.add(vet);
        given(vetRepository.findAll()).willReturn(vets);

        //WHEN
        Collection<Vet> findedVets = service.findVets();

        //THEN
        assertThat(findedVets).hasSize(1);
        then(vetRepository).should().findAll();
        then(vetRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findVets_shouldThrowException() {
        //GIVEN
        given(vetRepository.findAll()).willThrow(new DataAccessException("Boom") {});

        //WHEN

        //THEN
        assertThrows(DataAccessException.class, () -> {
            service.findVets();
        });
        then(vetRepository).should().findAll();
        then(vetRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    void findVisitsByPetId_shouldSucceed() {
        //GIVEN
        List<Visit> visits = new ArrayList<>();
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("Visite");
        Pet pet = new Pet();
        pet.setId(1);
        visit.setPet(pet);
        visits.add(visit);
        given(visitRepository.findByPetId(anyInt())).willReturn(visits);

        //WHEN
        Collection<Visit> findedVisits = service.findVisitsByPetId(1);

        //THEN
        assertThat(findedVisits).hasSize(1);
        then(visitRepository).should().findByPetId(anyInt());
        then(visitRepository).shouldHaveNoMoreInteractions();
    }
}
