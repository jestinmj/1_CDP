# Practical DevSecOps

## Introduction to DevSecOps

In this module, we will cover the introduction to DevSecOps, advantages and Core principles.

## Traditional SDLC

- Requirements: Gather Requirements from the client/costumer.
- Design: Design the software according to the requirements.
- Implementation: Implement the design agreed upon.
- Deploy: Deploy the software to the production.
- Maintain: Maintain of the software in production.

## History:
            
            [Wall of uncertainty]
Business Requirements ||| Development Teams 

### Then Agile Happened

Everything changed after agile, much shorter development cycles and faster deploys to production-

Speed with which changes are being made is beyond securit's (operations) reach.

[Enter the change: Agile]

    [Wall of uncertainty]
Developers ||| Operation

## DevOps

DevOps is a software engineering practice that aims at unifying software development (Dev) and software operation (Ops).

DevOps is a set of practices intended to reduce the time between commiting a change to a system and the change bein placed into normal production, while ensuring high quality - Bass, Weber and Zhu.

Development (Software Engineering) + Operations (Quality Assurance) = DevOps

## DevOps Cycle

A. Plan and Create: Plan and implement the code using source code management (SCM).
B. Verify: Test and verify the code does, what business wants.
C. Package: Package the code in a deployable artifact and test it in staging environment.
D. Release: Release the artefact as production ready after change/release approvals.
E. Configure: Configure the application/stack using configuration management.
F. Monitor: Monitor the application for its performance, security and compliance.

 [Wall of compliance]
DevOps ||| Security

## Security is Outnumbered!

Dev / Ops / Security
100 / 10  / 1

## DevSecOps

DevOps is a set of practices intended to reduce the time between commiting a change to a system and the change being placed into normal production, while ensuring high quality - Bass, Weber, and Zhu.

By definition, security is part of DevOps.

Development (Software Engineering) + Security (Quality Assurance) + Operations = DevSecOps

## DevSecOps Benefits

- **Resilience**: DevOps helps organisations in designing and implementing resilient systems.
- **Speed**: Speed is **competitive advantage** and DevOps helps to go to market faster.
- **Automation**: Automation helps to reduce complexity of modern systems and can scale as per needs.
- **Flexibility**: With ever changing technology, businesses have to be flexible and fast to deliver value to their customers otherwise **they risk** losing the **business**.
- **Reliability**: Customers need more reliable and available systems. DevOps reduces failure rates and provides faster **feedback**.

## How to DevSecOps?

### Core Values of DevOps

- **Culture**: DevOps is about breaking down barriers between teams; without culture other practices fail.
- **Measurement**: Measuring activities in CI/CD helps in informed decision making among teams.
- **Automation**: Often mistaken as DevOps itself but a very important aspect of the initiative.
- **Sharing**: Sharing tools, best practices etc,. among the teams/organization improves confidence for collaboration.

> **Conway's Law**
> Any organization that desingns a system (defined broadly) will produce a design, whose structure is a copy of the organization's communication structure.

## DevSecOps Implementation

So far we have looked at Principles and Ideas behind DevSecOps but how do we start implementing DevSecOps?

We can use the techniques (see towers your right hand side) discussed in this course to implement a full blown security pipeline.

- Shift Security Left: Use CI/CD pipeline to embed security.
- Self Service: Gives developers and operations visibility into security activities.
- Security Champions: Encourage security champions to pick security tasks.
- Everything as Code(EAC)
- Compliance as Code and hardening configuration management systems.
- Secure by Default: Use secure by default frameworks and services
- Use Maturity models: Use DevSecOps Maturity Models to improve further. 