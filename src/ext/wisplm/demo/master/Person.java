package ext.wisplm.demo.master;

public class Person {
	
	public static void main(String args){
		PersonMaster personMaster = new PersonMaster();
		personMaster.setName("张三");
		personMaster.setSex("男");
		
		Person p1 = new Person();
		p1.setAge(1);
		
		//复制p1
		Person p1Copy = new Person();
		
		p1Copy.setAge(2);
		
		Person p2 = p1Copy;
		
	}
	
	
	//版本=年龄
	private int age;
	
	private PersonMaster personMaster;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public PersonMaster getPersonMaster() {
		return personMaster;
	}

	public void setPersonMaster(PersonMaster personMaster) {
		this.personMaster = personMaster;
	}
	
	
}
