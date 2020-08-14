# Distributed-Services-using-WebServices
Project to create a Distributed Systems for Event Management 


Introduction
-------------------------------------------------
The basic idea of this project is to build a distributed event management system for a leading corporate event management company. This distributed system used by an event manager who manages the information about the events and customers who can book or cancel or swap an event across the company’s different branches Montreal Ottawa and Toronto using Web services.

The assignment involved many techniques, primarily the project revolved around 2 techniques:

1. Web Service
Web service is a standardized medium to propagate communication between the client and server applications on the World Wide Web.
Web services provide a common platform that allows multiple applications built on various programming languages to have the ability to communicate with each other

Popular Web Services Protocols are:
SOAP:
SOAP is known as the Simple Object Access Protocol.
SOAP was developed as an intermediate language so that applications built on various programming languages could talk quickly to each other and avoid the extreme development effort.

WSDL:
WSDL is known as the Web Services Description Language(WSDL).
WSDL is an XML-based file which tells the client application what the web service does and gives all the information required to connect to the web service.

REST:
REST stands for Representational State Transfer.
REST is used to build Web services that are lightweight, maintainable, and scalable.


2. UDP

UDP is a communication protocol that transmits independent packets over the network with no guarantee of arrival and no guarantee of the order of delivery.
Most communication over the internet takes place over the Transmission Control Protocol (TCP), however, UDP has its place which we will be exploring in the next section.
The UDP protocol provides a mode of network communication whereby applications send packets of data, called datagrams, to one another. A datagram is an independent, self-contained message sent over the network whose arrival, arrival time, and content are not guaranteed. The DatagramPacket and DatagramSocket classes in the java.net package implement system-independent datagram communication using UDP.
The purpose of using UDP over here is for the inter server communication. Separate server have been created for different departments and these server maintain the records for their respective departments courses e.g computer science server will maintain records related to computer science courses that have been offered at a particular term and what is the capacity of each courses.
