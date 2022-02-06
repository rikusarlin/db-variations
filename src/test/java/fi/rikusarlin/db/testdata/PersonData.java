package fi.rikusarlin.db.testdata;

import fi.rikusarlin.db.entity.Person;


public class PersonData {
	
	public static Person getPerson1() {
		Person p = new Person();
		p.setId(1);
		p.setFirstName("Rauli");
		p.setLastName("Wnape");
		return p;
	}
	
	public static Person getPerson2() {
		Person p = new Person();
		p.setId(2);
		p.setFirstName("Marke");
		p.setLastName("Peerakpe");
    	return p;
	}

	public static Person getPerson3() {
		Person p = new Person();
		p.setId(3);
		p.setFirstName("Walter");
		p.setLastName("Nutbekk");
		return p;
	}
	
	public static Person getPerson4() {
		Person p = new Person();
		p.setId(4);
		p.setFirstName("Suvi-Tuulia");
		p.setLastName("Retsetenpe");
		return p;
	}
}
