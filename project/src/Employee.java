

public class Employee {
 
 //attributes
 public String username;
 public String name;
 public String position;
 public String password; //amount of items on inventory
 
 //methods
 public Employee(String username,String name, String position,String password)
 {
  this.username = username; this.name = name; this.position = position; this.password = password;
 }
 
 String getUsername() {return username;}
 String getName() {return name;}
 String getPosition() {return position;}
 String getPassword() {return password;}      
 //void setUsername(String usename){this.username = username;};
 void setName(String name){this.name = name;};
 void setPosition(String position){this.position = position;};
 void setPassword(String password){this.password = password;};
}
