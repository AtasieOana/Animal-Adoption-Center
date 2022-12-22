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
- Vaccine
- MedicalRecord 
####  Other features: 
- Diet
- Cage

## Endpoints

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