===============================================================================================

******************************************
*          Starting the Services         *
******************************************

1. java -jar Healthcare-Service-1.0.0.jar	--> 	Will start the Healthcare service on port 8081
2. java -jar Hospital-Service-1.0.0.jar		-->	Will start the hospital service on port 8080



******************************************
*     Invoking the QSG Proxy service     *
******************************************

1. Deploy the WSO2QucikStartGuideCapp_1.0.0.car in WSO2 ESB.
2. By default the statistics and tracing enabled in the proxy artifact.
3. Invoke the service as following

curl -v -X POST "http://localhost:8280/services/ReserveAppointmentProxy" --header "Content-Type: application/json" -d @reserve_appointment.json -k -v

5. Request Payload

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

6. Response

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
		"patient": "Nadeeshaan Gunasinghe",
		"actualFee": 7000,
		"discount": 20,
		"discounted": 5600,
		"paymentID": "c5edddf3-d7d7-4756-8a03-5e8e31f2e716",
		"status": "Settled"
	}
}
