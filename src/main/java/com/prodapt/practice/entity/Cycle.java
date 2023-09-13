 package com.prodapt.practice.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
 
	public class Cycle {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    public int getVehiclesBorrowed() {
			return vehiclesBorrowed;
		}
		public void setVehiclesBorrowed(int vehiclesBorrowed) {
			this.vehiclesBorrowed = vehiclesBorrowed;
		}
		public int getVehiclesReturned() {
			return vehiclesReturned;
		}
		public void setVehiclesReturned(int vehiclesReturned) {
			this.vehiclesReturned = vehiclesReturned;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		@Column(unique = true)
		private String name;
	    private int stock;
	    private int vehiclesBorrowed;
	    private int vehiclesReturned; 

	     
	}


 
