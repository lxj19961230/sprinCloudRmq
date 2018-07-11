package com.xier.rabbit.rmq.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xier.rabbit.rmq.handler.ObjectProperty;
import com.xier.rabbit.rmq.handler.TypeConvert;
 
public class ObjectPropertyTest {

	@BeforeClass
	public static void testInit() { 
	}

	@AfterClass
	public static void testDestroy() throws Exception { 
	}


	@Test
	public void testObject() throws Exception { 
		String pathFormat="/%s/yyyy/MM/dd/HH/mm/ss/";
	    String memberFaceImage =String.format(pathFormat, "测试");	 
        SimpleDateFormat dateFormater = new SimpleDateFormat(memberFaceImage);
        Date date=new Date();
        String filePath = dateFormater.format(date)+"aa.jpg";
        System.out.println(filePath);
		Object object = new PersonRecognizeLog();
		byte[] c=TypeConvert.Object2ByteArray(object);
		Object d=TypeConvert.byteArray2Object(c);
		System.out.println(d.getClass().getName());
	}

	@Test
	public void testProperty() throws Exception { 
		long start=System.currentTimeMillis();
		for(int i=0;i<1;i++){
			PersonRecognizeLog object = new PersonRecognizeLog();
			object.setPersonId(1234567890);
			 Object obj = ObjectProperty.getFieldValueByMethod("getPersonId", object);
		}
		long end=System.currentTimeMillis();		
		 System.out.println("reflect excute times:"+(end -start));
			start=System.currentTimeMillis();
			for(int i=0;i<1;i++){
				PersonRecognizeLog object = new PersonRecognizeLog();
				object.setPersonId(1234567890);
				 Object obj =object.getPersonId();
			}
			end=System.currentTimeMillis();		
			 System.out.println("get excute times:"+(end -start));
	}
 
}