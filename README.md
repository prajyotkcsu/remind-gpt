# RemindGPT

## Overview
RemindGPT is a simple yet effective task management tool designed to help you stay organized and focused throughout your day. Its core functionality revolves around parsing your wishlist using OpenAI API (prompt engineering) and coming up with a tailored list of task. These tasks are cheery picked from across different categorites. Right now, the app supports four major categories of tasks. They are enum values: Fitness, Social, Self-Development, and Responsibilities.

## How it is done.
Postman acts like the frontend for the APIs
1) Send a request to localhost:8084/whishlist and let OpenAI API interprets your wishlish. This involves categorizing individual tasks into the four types along with possible duration and priority.
2) Send request to OpenAI on https://api.openai.com/v1/chat/completions as,
   {
  "model": "gpt-3.5-turbo",
  "messages": [

    {"role": "user", "content": "Categorize tasks written within <cycling, taking a walk, calling mom, solving leetcode, commit code,going to the bank, clling amazon for refun> into only (self-help, hygine, mindfulness, work, social, fitness, responsibility)"}
  ]
}

3) Response to this from OpenAI API, see how ChatGPT interpreted Strings of tasks into apt categories
   Cycling: Fitness\n- Taking a walk: Fitness\n- Calling mom: Social\n- Solving LeetCode: Work\n- Commit code: Work\n- Going to the bank: Responsibility\n- Calling Amazon for refund: Self-help
4) Further, upon http response from OpenAI API, the app sends each task to the Kafka topic assist-task as key value pair. Key Fitness and value is Cycling
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
3. Install dependencies: `npm install` (or relevant command for your project)

## Usage
1. Launch the application.
2. Enter your wishlist for the day.
3. When prompted, input the time you have for your break.
4. Receive personalized task recommendations.
5. Enjoy a productive break with RemindGPT's assistance.

## Contributing

