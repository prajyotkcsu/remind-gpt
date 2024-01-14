# RemindGPT

## Overview
RemindGPT is a simple yet effective task management tool designed to help you stay organized and focused throughout your day. Its core functionality revolves around parsing your wishlist using OpenAI API (prompt engineering) and coming up with a tailored list of task. These tasks are cheery picked from across different categorites. Right now, ethe app supports five major categories of tasks. They are enum values: Fitness, Social, Self-Development, and Responsibilities, Work.

## How it is done.
Postman acts like the frontend for the APIs
1) Send request to the app on localhost:8084/whishlist as,
   {
wishlist:["cycling","taking a walk","calling mom tonight", "putting trash out", "commit code on git","going to the bank", "calling amazon customer service for a refund"]
   }
3) App utilizes OpenAI API https://api.openai.com/v1/chat/completions. API transforms your wishlist into actionable tasks and puts them into categories. Majorly four, Fitness, Social, Work, Responsibility, Self-dev. Example as shown below,
   1) Cycling: Fitness,
   2) Taking a walk: Fitness,
   3) Calling mom: Social,
   4) Commit code: Work,
   5) Going to the bank: Responsibility,
   6) Calling Amazon for refund: Self-help
   
4) Further, upon receiving this as a response from the AI, app sends the curation to the Kafka server, and appends partitions with values. Here, Kafka has a topic: "assist-topic", with 5 partitions each dedicated for five separate categories of tasks.
5) Until now Kafka producer is doing its part. As soon as user takes a break, an API triggers the application at localhost:8084/tasks
6) The application now fetches List<Tasks> from the Kafka topic. It constructs the list as list of tasks from each partition/category based on the break duration. Say calling a friend would need 10 min+ taking a walk would require 5 min and both can be done together. These kind of logic are built using OpenAI API. The core of this project is storing key value in Kafka, and logically presenting tasks from each partition and adjusting the offset pointer.

## Features
- **Smart Task Reminders**: Rather than overwhelming you with a list of all remaining tasks, RemindGPT intelligently considers the time you have before your break. It prompts you to input the available time, and then suggests tasks that can be achieved within that timeframe.

- **Personalized Assistance**: Think of RemindGPT as your friend. It assists you in making the most out of your break time by recommending tasks that align with the time you have available.

## Getting Started
To start using RemindGPT, follow these simple steps:
1. [Installation](#installation): Instructions on how to install and set up RemindGPT.
2. [Usage](#usage): A guide on how to use the application and take advantage of its features.

## Installation
1. Clone the repository: `git clone https://github.com/your-username/RemindGPT.git`
2. Navigate to the project directory: `cd RemindGPT`
3. Use docker file to run project with its dependencies

## Usage
1. Launch the application.
2. Enter your wishlist for the day.
3. When prompted, input the time you have for your break.
4. Receive personalized task recommendations.
5. Enjoy a productive break with RemindGPT's assistance.

## Contributing

