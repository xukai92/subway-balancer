# NYC AI Hackathon: AI for Public Good NY

Join A16Z and the founding Chief AI Officer @ NYC Mayor's Office to build AI projects for public good, focused on: transportation, healthcare, safety, transparency, sustainability, energy, governance, efficiency, and automation

--------------

## Subway-balancer


Living in New York, we often found ourselves frustrated while trying to choose the least crowded subway car during the commute, leading to an uncomfortable journey when our estimations were off. In particular, during the morning and evening rush hours on the subway.


## Solution
We aim to build a system to guide passenger to reach to carriage with more space.

Out system consists two steps: (1) people counting and (2) traffic routing.

- This step operates by capturing real-time images of the subway, and then employing YOLO to count the number of passengers in each carriage.
- Upon acquiring the number data, we run an route balancing algorithm. This step provides real-time instructions to passengers to move towards more emtpy carriages.

Through this approach, we are able to balance the load, ensuring a journey that is both more comfortable and more convenient for everyone.

--------------

## Demo

![image](https://github.com/xukai92/subway-balancer/assets/20774071/4168f4ff-4d97-4588-be8f-8afefe77c5cd)


See https://github.com/xukai92/subway-balancer/blob/main/demo/illustration.ipynb for demo
