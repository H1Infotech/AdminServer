import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    
    public static void main(String[] args) {
    	String pwd = (new BCryptPasswordEncoder()).encode("chris");
    	System.out.println(pwd);
    }
}
