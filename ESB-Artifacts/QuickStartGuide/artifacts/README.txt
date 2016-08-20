===============================================================================================

1. Deploy the WSO2QuickStartGuideCapp_1.0.0.car in WSO2 ESB.
2. Execute the following command to start the Hospital service on port 8080:
   java -jar Hospital-Service-2.0.0.jar
3. Execute the following command to send a request to the REST API:
   curl -v -X POST "http://localhost:8280/hospitalservice/reserve" --header "Content-Type: application/json" -d @reserve_appointment.json -k -v


Following is a sample request you can send:

{
	"reserveRequest": {
		"patientDetails": {
			"name": "Mark Smith",
			"dob": "1990-03-19",
			"ssn": "ASJK-asnda-AAA",
			"address": "100 MAIN ST, SEATTLE WA 98104, USA",
			"phone": "0770596754",
			"email": "marksm@mymail.com"
		},
		"doctor": "thomas collins",
		"hospital": "grand oak community hospital",
		"category": "surgery",
		"cardNo": "7844481124110331"
	}
}

Following is the response that will be returned for the request:

{
	"appointmentNumber": 40,
	"Doctor": {
		"fee": 7000,
		"name": "thomas collins",
		"availability": "9.00 a.m - 11.00 a.m",
		"hospital": "grand oak community hospital",
		"category": "surgery"
	},
	"Payment": {
		"patient": "Mark Smith",
		"actualFee": 7000,
		"discount": 20,
		"discounted": 5600,
		"paymentID": "c5edddf3-d7d7-4756-8a03-5e8e31f2e716",
		"status": "Settled"
	}
}
