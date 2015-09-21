public class User{
  private String name;
  private int password;
  public User(String user, int password){
     this.name=user;
     this.password=password;
  }
  
  public boolean checkPsw(int password){
    return this.password == password;
  }
  
  public String name(){
    return this.name;
  }
}
