1) Download the souce files
2) Place soucre files in a seperate folder and open it with your editor
3) To retrieve the Meta Data, run the DBMetaData.java file
4) To manipulate the database tables with CRUD operations, you can access the following function (declared inside of DBOperations) from the DBTest.java file.
   Access the function by creating a new object or unsing the existing obj variable.
   i. To insert new entries, type obj.insertTABLENAME(attribute_value, attribute_value); // place string data inside of double quotation mark " "
   ii. To retrieve data from a table, type obj.retrieveTABLENAME();
   iii. To update a value in a table, type obj.updateTABLENAME();
   iv. To delete entries from a table, type obj.deleteTABLENAME();
     
