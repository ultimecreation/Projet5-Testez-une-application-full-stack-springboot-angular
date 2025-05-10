# Yoga-app
- Pour ce projet,j'ai été chargé de :
  -  réaliser des tests unitaires et fonctionnels sur l'application (controllers,security,services....)
  - Réaliser les tests E2E avec cypress

### Tests backend
 ![yoga-app-backend-coverage](https://github.com/user-attachments/assets/d57cec42-06f7-44db-8f3c-bcac139e13df)

### Frontend unit and integration tests

 ![yoga-app-front-coverage](https://github.com/user-attachments/assets/3805e741-3b51-46ec-b5ad-19b4750a1475)

### Fullstack Cypress coverage
![yoga-app-cypress-coverage](https://github.com/user-attachments/assets/f713e373-52de-4f90-abfb-a3db41b61bae)


## Local project setup

### Setup Mysql and PhpMyAdmin

- You can follow this [tutorial](https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql)
- You can also use [Xampp to get mysql and phpMyAdmin easily](https://www.apachefriends.org/)
- Or, if you have Docker installed on your machine, you can follow this 5 min [tutorial](https://tecadmin.net/docker-compose-for-mysql-with-phpmyadmin/)
- update the backend environment file at `back/src/main/resources/application.properties`

SQL script for creating the schema is available at the root level `ressources/sql/script.sql`
![sql-and-json](https://github.com/user-attachments/assets/63560c19-b141-4149-be6e-f7cbda6e59a7)

By default the admin account is:

- login: yoga@studio.com
- password: test!1234


### frontend

- Clone this repository on your local machine
- Navigate to your `front` folder and run the command `npm install` in your integrated terminal

![front_1](https://github.com/user-attachments/assets/3b2bf803-6249-4498-ac89-89df0e100de2)

- You can start the project with the follwing command `npm start`.
- You're project is available at [http://localhost:4200/](http://localhost:4200/)

  ![front_2](https://github.com/user-attachments/assets/a04c4cf3-ef9b-4fbb-8638-bfb251cd89da)

### backend

- Navigate to your `back` folder.
- Find your main app file located at `back/src/main/java/com/openclassrooms/starterjwt/SpringBootSecurityJwtApplication.java` and click on the `Run` button or on the arraow on the top right corner.

  ![back_2](https://github.com/user-attachments/assets/0ff7160c-edc5-49c3-99f3-927e22e7fdf8)

## Testing

### Backend tests

- open your integrated terminal and navigate to your `back` folder
- run the command `mvn clean test`

### Frontend tests
Several commands are available : 
- `npm run e2e` for e2e tests
- `npm run e2e:coverage` for e2e tests coverage (the html file is available at `front/coverage/lcov-report/index.html`
-  `npm run test` to launch unit tests only one time
-  `npm run test:watch` for development, the tests are replayed after each change in the code


