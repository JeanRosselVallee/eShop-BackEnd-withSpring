# Instructions
.

## 1 Released Files 
The files to be taken into account are:
#### - eShop-FrontEnd-withAngular-master_jv2.zip
#### - eShop-BackEnd-withSpring-master_jv2.zip
### Note: 
These version of the application has been released on July 9th afternoon. If the delay is an issue, please take into account the version of the application released on July 8th midnight with the suffix "jv1" instead of "jv2". The differences between the 2 versions are minor and impact only the appeareance of the application since no new methods or functions were added. 

## 2 Create database "shopspring"
The SQL script to create the tables with data is:
#### eShop-BackEnd-withSpring-master_jv1.zip\eShop-BackEnd-withSpring-master\src\sql\FullDb_shopspring.sql

## 3 Launch the Back-end server
run server: mvn spring-boot:run

## 4 Launch the Front-end server
ng serve

## 5 Go to web page http://localhost:4200
Enjoy web surfing of the eShop

.

# Useful information for testing the implemented features

## Admin user "f" with password "ff"
In order to test the features that are only accessible to an admin user, there are two choices:
- an admin account may be created via MyPhpAdmin or Mysql
- use the credentials of the user whose email is "f" and his password "ff".

## Implementation of Users' features
The following features related to Users are displayed in the same uri "/users" and at the same time:
- login as a User. 
  - the name of the user is displayed under the Logo. 
  - if the user has an admin role, then the administration links are displayed in the menu
- create a User
- list all Users 
- the following features have only been implemented in the back-end:
    - delete a user
    - update a user

## Implementation of Carts' features
- only the back-end side has been implemented with the following features:
  - add a cart. A cart is created when his owner is created 
  - delete a cart. The temporary apikey used for this feature is "123".
  - list all carts. The temporary apikey is "123".




