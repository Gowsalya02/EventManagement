package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.AdminDao;
import dao.ClientDao;
import dao.ClientEventDao;
import dao.ClientServiceDao;
import dao.ServiceDao;
import dto.Admin;
import dto.Client;
import dto.ClientEvent;
import dto.ClientService;
import dto.EventType;
import dto.Service;

public class EventMangementController 
{
	EntityManagerFactory emf=Persistence.createEntityManagerFactory("gowsi");
	EntityManager em=emf.createEntityManager();
	EntityTransaction et=em.getTransaction();
	
	Scanner sc=new Scanner(System.in);
	
	Admin admin=new Admin();
	Client client=new Client();
	ClientEvent ce=new ClientEvent();
	ClientService cs=new ClientService();
	Service service=new Service();
	AdminDao adminDao=new AdminDao();
	ServiceDao serviceDao=new ServiceDao();
	ClientDao clientDao=new ClientDao();
	ClientServiceDao csDao=new ClientServiceDao();
	ClientEventDao ceDao=new ClientEventDao();
	
	public static void main(String[] args)
	{
		//1..Save Admin data 
		
		EventMangementController emc=new EventMangementController();
//		System.out.println(em.saveAdmin());
		
        //2.admin login
		
//		System.out.println(emc.adminLogin());
//		System.out.println(emc.createService());
	
		//3.get server available
		
//		System.out.println(emc.getAllServices());
		
		//4.update Service 
		
//		System.out.println(emc.updateService());
		
		//5.delete service
//		System.out.println(emc.deleteService());
		
		//6.client signup
		
//		System.out.println(emc.ClientSignup());
		
		//7.client login 
		
//		System.out.println(emc.clientLogin());
		
		//8.createClientEvent
		
		System.out.println(emc.createClientEvent());
		
	}
	public Admin saveAdmin() 
	{
		Admin a=new Admin();
		
		System.out.println("Enter admin name: ");
		a.setAdminName(sc.next());
		System.out.println("Enter admin mail: ");
		a.setAdminMail(sc.next());
		System.out.println("Enter admin password: ");
		a.setAdminPassword(sc.next());
		System.out.println("Enter admin contact number: ");
		a.setAdminContact(sc.nextLong());
		
		return adminDao.saveAdmin(a);
	}
	public Admin adminLogin() 
	{
		System.out.println("enter email of admin: ");
		String email=sc.next();
		System.out.println("enter valid password: ");
		String password=sc.next();
		String q="select a from Admin a";
		Query query=em.createQuery(q);
		Admin admin=(Admin) query.getSingleResult();
		if(admin.getAdminMail().equals(email) && admin.getAdminPassword().equals(password))
		{
			return admin;
		}
		else return null;
	}
	public Service createService()
	{
		
		Admin exAdmin=adminLogin();
		if(exAdmin != null)
		{
		Service s=new Service();
		System.out.println("Enter Service name: ");
		s.setServiceName(sc.next());
		sc.nextLine();
		System.out.println("Enter no of days: ");
		s.setNoOfDays(sc.nextInt());
		System.out.println("Enter service cost per person: ");
		s.setServiceCostPerPerson(sc.nextDouble());
		System.out.println("Enter cost per day: ");
		s.setCostPerDay(sc.nextDouble());
		
		Service service= serviceDao.saveService(s);
		exAdmin.getServices().add(service);
		adminDao.updateAdmin(exAdmin.getAdminId() , exAdmin );
		return service;
		}
		return null;
	}


	public List< Service> getAllServices()
	{
		System.out.println("enter the admin cerdential to proceed ");
		Admin exAdmin=adminLogin();
		if(exAdmin!=null)
		{
			return exAdmin.getServices();
		}
		return null;
	}
	
	public String updateService()
	{
		Scanner sc = new Scanner(System.in);
		List<Service> services=getAllServices();
		for(Service s : services)
		{
			System.out.println("serviceId  " + "serviceName  " + "serviceCostPerDay  "
				+ "serviceCostPerPerson  ");
			System.out.println("  " + s.getServiceId()+"         "+s.getServiceName()+"         "+s.getCostPerDay()+"      "+s.getServiceCostPerPerson());
			
		}
		System.out.println("Choose Service Id to Update The Service");
		int id = sc.nextInt();
		Service tobeUpdated = serviceDao.findService(id);
		System.out.println("Enter Updated cost per person");
		double costperperson = sc.nextDouble();
		System.out.println("Enter Updated cost per day");
		double costperday = sc.nextDouble();
		tobeUpdated.setCostPerDay(costperday);
		tobeUpdated.setServiceCostPerPerson(costperperson);
		
		Service updated =serviceDao.updatService(id,tobeUpdated);
		if(updated!=null)
		{
			return "service update success";
		}
		return null;
	}
	
	public List<Service> deleteService()
	{
		Admin exAdmin=adminLogin();
		if(exAdmin!= null)
			{
				List<Service> services=exAdmin.getServices();
				for(Service s : services)
				{
					System.out.println("serviceId  " + "serviceName  " + "serviceCostPerDay  " + "serviceCostPerPerson  ");
					System.out.println("  " + s.getServiceId()+"         "+s.getServiceName()+"         "+s.getCostPerDay()+"      "+s.getServiceCostPerPerson());
					
				}
				System.out.println("Choose Service Id to Delete The Service");
				int id = sc.nextInt();
				List<Service> newList = new ArrayList<Service>();
				for (Service service : services) 
				{
					if(service.getServiceId()!=id)
					{
						newList.add(service);
					}
				}
				exAdmin.setServices(newList);
				adminDao.updateAdmin(exAdmin.getAdminId(),exAdmin);
				serviceDao.deleteService(id);
				
				return services;
			}
			return null;
	}
	public Client ClientSignup()
	{
		Client client=new Client();
		System.out.println("enter the clientname");
		client.setClientName(sc.next());
		System.out.println("enter the contact");
		client.setClientContact(sc.nextLong());
		System.out.println("enter the mail");
		client.setClientEmail(sc.next());
		System.out.println("enter the password");
		client.setClientPassword(sc.next());
		
		return clientDao.saveClient(client);
		
		
	}
	
	public Client clientLogin()
	{
		System.out.println("enter the client mail to login");
		String mail=sc.next();
		System.out.println("enter the client password");
		String password=sc.next();
		Query query=em.createQuery("select c from Client c where c.clientEmail=?1");
		query.setParameter(1, mail);
		Client exClient=(Client)query.getSingleResult();
		if(exClient!=null)
		{
			if(exClient.getClientPassword().equals(password))
			{
				System.out.println("success");
				return exClient;
			}
			else 
			{
				System.out.println("not success");
				
			}
		}
	return null;
	}
	
	public String createClientEvent()
	{
		Client exClient=clientLogin();
		List<ClientEvent> clientEvents=new ArrayList<ClientEvent>();
		if(exClient!=null)
		{
			ClientEvent ce=new ClientEvent();
			EventType[] et=EventType.values();
			int i=1;
			for (EventType eventType : et) 
			{
				System.out.println((i++)+" "+eventType);
				
			}
			
			System.out.println("Enter the key to choose event type");
			
			int key =sc.nextInt();
			switch (key) 
			{
			case  1:ce.setEventType(EventType.Marriege);break;
			case  2:ce.setEventType(EventType.Engagement);;break;
			case  3:ce.setEventType(EventType.Birthday);break;
			case  4:ce.setEventType(EventType.Anniversary);break;
			case  5:ce.setEventType(EventType.Babyshower);break;
			case  6:ce.setEventType(EventType.Reunion);break;
			case  7:ce.setEventType(EventType.NamingCeremony);break;
			case  8:ce.setEventType(EventType.BachelorParty);break;
			default:System.out.println("invalid input");break;
			}
			
			System.out.println("Enter no of people attending  the event");
			ce.setNoOfPeople(sc.nextInt());
			System.out.println("Enter no of days the event happend");
			ce.setNoOfDays(sc.nextInt());
			System.out.println("enter the location for the event");
			ce.setLocation(sc.next());
			System.out.println("Enter starting date of the event in yyyy-mm-dd formate");
			ce.setStartDate(LocalDate.parse(sc.next()));
			
			ce.setClient(exClient);
			System.out.println("Enter adding Count of Services");
			int count=sc.nextInt();
			
			double eventCost=0;
			
			List<ClientService> clientServices=new ArrayList<ClientService>();
			for (int j = 0; j <count; j++) 
			{
				ClientService cs=new ClientService();
				List<Service> services=getAllServices();
				
				System.out.println("/t----Service List-----");
				for (Service service : services) {
					System.out.println(service);
				}
				
				System.out.println("Enter Service Id");
				int value = sc.nextInt();
				Service s1 = serviceDao.findService(value);
				cs.setClientServiceName(s1.getServiceName());
				cs.setClientServiceNoOfDays(ce.getNoOfDays());
				cs.setClientServiceCostPerPerson(s1.getServiceCostPerPerson());
				cs.setClientServiceCost(ce.getNoOfPeople() * cs.getClientServiceCostPerPerson() *  cs.getClientServiceNoOfDays());
				eventCost = eventCost + cs.getClientServiceCost();
				clientServices.add(cs);
				ClientService cs1= csDao.saveClientService(cs);
			}
			ce.setTotalCost(eventCost);
			ce.setClientService(clientServices);
			clientEvents.add(ce);
			exClient.setClientEvent(clientEvents);
			Client updatedClient = clientDao.updateClient(exClient.getClientId(),exClient);
			if(updatedClient != null)
			{
				return "Client Event Added";
			}
			
			ClientEvent savedClientEvent=ceDao.saveClientEvent(ce);
			exClient.getClientEvent().add(savedClientEvent);
			clientDao.updateClient( exClient.getClientId(),exClient);
		}													
		return "Client not added";
	}

	
	public String addEventService(Service service)
	{
    System.out.println("------Add Client Service--------");
	Client exClient = clientLogin();
	if(exClient != null)
	{
		List<ClientEvent> exClientEvents = exClient.getClientEvent();
		System.out.println("Enter Client Event Id : "); 
		int exClientEventId = sc.nextInt();
		int count = 0;
		for(ClientEvent events : exClientEvents)
		{
			if(events.getClientEventId() == exClientEventId)
			{
				count ++;
				double eventCost = events.getTotalCost();
				List<ClientService> exClientServices = events.getClientService();
				System.out.println("Enter Service Adding Count : "); 
				int serviceCount = sc.nextInt();
				for(int i=1;i<=serviceCount;i++)
				{
					ClientService cs = new ClientService();
					List<Service> listOfServices = getAllServices();
					System.out.println("\t ----- Service Lists -----");
					for (Service services : listOfServices) 
					{
						System.out.println(services);
					}
					System.out.print("Enter Service Id :");
					int svalue = sc.nextInt();
					Service s1 = serviceDao.findService(svalue);
					cs.setClientServiceName(s1.getServiceName());
					cs.setClientServiceNoOfDays(events.getNoOfDays());
					cs.setClientServiceCostPerPerson(s1.getServiceCostPerPerson());
					cs.setClientServiceCost(events.getNoOfPeople() * cs.getClientServiceCostPerPerson() * cs.getClientServiceNoOfDays());
					eventCost = eventCost + cs.getClientServiceCost();
					exClientServices.add(cs);
					ClientService cs1= csDao.saveClientService(cs);
				}
				
				events.setTotalCost(count);
				events.setClientService(exClientServices);
				ClientEvent ce1 = ceDao.updateClientEvent(events.getClientEventId(),events);   
				if(ce1 != null)
				{
					return "Client Service Added";
				}
			}
		}
		if(count == 0)
			return "Invalid Id Event Not Found";
	}
	return "Client Service Not Added";
	}

}
