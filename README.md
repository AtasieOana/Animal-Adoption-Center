# Animal Adoption Center Management

**Project developed in Java with Spring Boot**

## Project purpose
<div align="justify"> The purpose of the application is to help the administrator and employees of an animal adoption center to process more easily the information related to the animals brought to the center, as well as the information necessary for their care. </div>

## Business requirements
1. Viewing employees (caretakers and vets) records
2. Administering the duties of employees (caretakers and vets)
3. Viewing all the animals in the center along with details related to their care
4. Viewing the center's animal records
5. Marking the adoption of an animal by a customer
6. Visualization of statistics related to the center's clients
7. Keeping an animal's health record
8. Viewing the vaccines administered to an animal
9. Keeping records of existing vaccines in stock
10. Keeping records of the diets in stock

## Five main features
- Employee
    - used for the administration of employees in the center
    - an employee is characterized by name, surname, phone number, date of employment and salary
    - the center contains two types of employees: veterinarians and caregivers; in the table associated with employees, veterinarians and caregivers are separated by fields responsibility (null for veterinarian) and experience (null for caregiver)
- Animal
  - used to retain information related to the animals brought to the center: the type of animal, the date the animal was found and the year of birth
  - the class keeps track of the animals and how they should be cared for: the food they eat, the cage they stay in
  - for an animal, its adoption can be marked
- Client
  - used to process information about clients who adopt animals and statistics related to them (average age and majority gender)
- Vaccine
  - animals are vaccinated to maintain their health
  - there is a quantity of vaccines with a certain name in stock, which expires on a certain date saved in the database in order not to use expired doses
- MedicalRecord 
  - the animals are controlled and vaccinated regularly by vets, this information being noted in the medical records
####  Other features: 
- Diet
  - used to manage the information about the animals diets that were brought to the center
- Cage
  - the animals are kept in cages cared by caretakers, and a maximum number of animals can fit in a cage

## Endpoints

More info about the endpoints can be seen in [Swagger Documentation](swagger-documentation.html).

#### Employee endpoints: </br>
📍 **GET:** Getting all employees who have the role of vet <br />
&emsp;&emsp;&emsp; - path: <ins>/vets</ins> <br />
📍 **GET:** Getting a vet by first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/vets/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **POST:** Adding a new vet employee to the system <br />
&emsp;&emsp;&emsp; - path: <ins>/vets</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody EmployeeDto <br />
📍 **DELETE:** Delete a vet with a given first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/vets/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **PUT:** Update the info of a vet <br />
&emsp;&emsp;&emsp; - path: <ins>/vets/{oldFirstName}/{oldLastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String oldFirstName, String oldLastName, RequestBody EmployeeDto<br />
📍 **PUT:** Increase all vets' salaries by a certain percentage <br />
&emsp;&emsp;&emsp; - path: <ins>/vets/updateAllSalaries/{percent}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Integer percent <br />
📍 **GET:** Getting all employees who have the role of caretaker <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers</ins> <br />
📍 **GET:** Getting a caretaker by first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **POST:** Adding a new caretaker employee to the system <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody EmployeeDto <br />
📍 **DELETE:** Delete a caretaker with a given first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **PUT:** Update the info of a caretaker <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers/{oldFirstName}/{oldLastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String oldFirstName, String oldLastName, RequestBody EmployeeDto<br />
📍 **PUT:** Increase all caretakers' salaries by a certain percentage <br />
&emsp;&emsp;&emsp; - path: <ins>/caretakers/updateAllSalaries/{percent}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Integer percent <br />

#### Animal endpoints: </br>
📍 **GET:** Getting all animals from the center <br />
&emsp;&emsp;&emsp; - path: <ins>/animals</ins> <br />
📍 **GET:** Getting all animals from a cage <br />
&emsp;&emsp;&emsp; - path: <ins>/animals/inCage/{cageId}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long cageId <br />
📍 **GET:** Getting the animal that has been in the center for the longest time <br />
&emsp;&emsp;&emsp; - path: <ins>/animals/getOldestAnimalInCenter</ins> <br />
📍 **POST:** Adding a new found animal <br />
&emsp;&emsp;&emsp; - path: <ins>/animals</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody PartialAnimalDto <br />
📍 **DELETE:** Delete from database all the animals that were adopted <br />
&emsp;&emsp;&emsp; - path: <ins>/animals/deleteAdoptedAnimals</ins> <br />
📍 **PUT:** Mark the adoption of a animal by a client <br />
&emsp;&emsp;&emsp; - path: <ins>/animals/adoptAnimal/{animalId}/{clientFirstName}/{clientLastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long animalId, String clientFirstName, String clientLastName<br />
📍 **PUT:** Add to a animal the cage he was put in <br />
&emsp;&emsp;&emsp; - path: <ins>/animals/putAnimalInCage/{animalId}/{cageId}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long animalId, Long cageId<br />

#### Client endpoints: </br>
📍 **GET:** Getting all center clients <br />
&emsp;&emsp;&emsp; - path: <ins>/clients</ins> <br />
📍 **GET:** Getting a client by first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/clients/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **POST:** Adding a new client to the system <br />
&emsp;&emsp;&emsp; - path: <ins>/clients</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody ClientDto <br />
📍 **GET:** Calculating the average age for the clients <br />
&emsp;&emsp;&emsp; - path: <ins>/clients/avgAgeForClient</ins> <br />
📍 **GET:** Calculating the number of women and male clients <br />
&emsp;&emsp;&emsp; - path: <ins>/clients/clientsNumberByGender</ins> <br />
📍 **DELETE:** Delete a client with a given first and last name <br />
&emsp;&emsp;&emsp; - path: <ins>/clients/{firstName}/{lastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String firstName, String lastName<br />
📍 **PUT:** Update the info (first name, last name, phone number) of a client <br />
&emsp;&emsp;&emsp; - path: <ins>/clients/{oldFirstName}/{oldLastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String oldFirstName, String oldLastName, RequestBody PartialClientDto<br />

#### Vaccine endpoints: </br>
📍 **GET:** Viewing the vaccines in the order of expiration <br />
&emsp;&emsp;&emsp; - path: <ins>/vaccines</ins> <br />
📍 **POST:** Addition of a new vaccine characterized by name, quantity brought and expiration date <br />
&emsp;&emsp;&emsp; - path: <ins>/vaccines</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody VaccineDto <br />
📍 **DELETE:** Deleting expired vaccines from the system, equivalent to throwing them in the trash <br />
&emsp;&emsp;&emsp; - path: <ins>/vaccines/deleteExpiredVaccines</ins> <br />
📍 **GET:** Viewing the vaccines with empty stock, to know what needs to be repurchased <br />
&emsp;&emsp;&emsp; - path: <ins>/vaccines/getAllVaccinesWithEmptyStock</ins> <br />
📍 **PUT:** Changing the stock and expiration date of an existing vaccine <br />
&emsp;&emsp;&emsp; - path: <ins>/vaccines/{vaccineName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String vaccineName, RequestBody PartialVaccineDto<br />

#### Medical Record endpoints: </br>
📍 **GET:** Viewing all the medical records associated with an animal <br />
&emsp;&emsp;&emsp; - path: <ins>/medicalRecords/getMedicalRecordsForAnimal/{animalId}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long animalId<br />
📍 **POST:** Saving a new medical record <br />
&emsp;&emsp;&emsp; - path: <ins>/medicalRecords</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody MedicalRecordDto <br />
📍 **DELETE:** Deleting medical records prior to a date to avoid retaining information that is no longer relevant <br />
&emsp;&emsp;&emsp; - path: <ins>/medicalRecords/deleteMedicalRecordBeforeDate/{date}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Date date<br />
📍 **GET:** Finding the most used vaccine from medical records <br />
&emsp;&emsp;&emsp; - path: <ins>/medicalRecords/getMostUsedVaccine</ins> <br />
📍 **PUT:** If the same vaccine is repeated on an animal, only the state of health and the generation date of the medical record can be updated <br />
&emsp;&emsp;&emsp; - path: <ins>/medicalRecords/{id}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long id, RequestBody PartialMedicalRecordDto<br />

#### Diet endpoints: </br>
📍 **GET:** Viewing all the diets bought <br />
&emsp;&emsp;&emsp; - path: <ins>/diets/getAllDiets</ins> <br />
📍 **POST:** Addition of a new diet characterized by type and quantity brought <br />
&emsp;&emsp;&emsp; - path: <ins>/diets</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody DietDto <br />
📍 **GET:** Viewing the diets with empty stock <br />
&emsp;&emsp;&emsp; - path: <ins>/diets/getAllDietsWithEmptyStock</ins> <br />
📍 **DELETE:** Deleting a useless diet type, this operation can only be performed if the stock is empty <br />
&emsp;&emsp;&emsp; - path: <ins>/diets/deleteIfStockEmpty/{dietType} </ins> <br />
&emsp;&emsp;&emsp; - parameters: String dietType <br />
📍 **PUT:** Updating the info about a specific diet <br />
&emsp;&emsp;&emsp; - path: <ins>/diets/{dietType}</ins> <br />
&emsp;&emsp;&emsp; - parameters: String dietType, RequestBody DietDto<br />

#### Cage endpoints: </br>
📍 **GET:** Getting cages without caretakers, to know what assignments are necessary <br />
&emsp;&emsp;&emsp; - path: <ins>/cages/getCagesWithoutACaretaker</ins> <br />
📍 **GET:** Getting a cage by id <br />
&emsp;&emsp;&emsp; - path: <ins>/cages/{id}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long id<br />
📍 **POST:** Addition of a new cage characterized by a number a places and possibly a caretaker <br />
&emsp;&emsp;&emsp; - path: <ins>/cages</ins> <br />
&emsp;&emsp;&emsp; - parameters: RequestBody CageDto <br />
📍 **DELETE:** Removal of a cage with a specific id <br />
&emsp;&emsp;&emsp; - path: <ins>/cages/{id} </ins> <br />
&emsp;&emsp;&emsp; - parameters: Long id <br />
📍 **PATCH:** Assigning a caretaker to a cage <br />
&emsp;&emsp;&emsp; - path: <ins>/cages/updateCageCaretaker/{id}/{caretakerFirstName}/{caretakerLastName}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long id, String caretakerFirstName, String caretakerLastName<br />
📍 **PATCH:** Changing the number of places in a cag <br />
&emsp;&emsp;&emsp; - path: <ins>/cages/updateCagePlaces/{id}/{nrPlaces}</ins> <br />
&emsp;&emsp;&emsp; - parameters: Long id, Integer nrPlaces<br />