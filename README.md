# RemindGPT

## Overview
RemindGPT is a simple yet effective task management tool designed to help you stay organized and focused throughout your day. Its core functionality revolves around parsing your wishlist using OpenAI API (prompt engineering) and coming up with a tailored list of task. These tasks are cheery picked from across different categorites. Right now, ethe app supports five major categories of tasks. They are enum values: Fitness, Social, Self-Development, Responsibilities, and Work.

## Features
- **Smart Task Reminders**: Rather than overwhelming you with a list of all remaining tasks, RemindGPT intelligently considers the time you have before your break. It prompts you to input the available time, and then suggests tasks that can be achieved within that timeframe.

- **Personalized Assistance**: Think of RemindGPT as your friend. It assists you in making the most out of your break time by recommending tasks that align with the time you have available.

## Procedure to use the app
1) Send request to the app
 ```
   localhost:8084/reminder/api/task/produce
```
  
![image](https://github.com/prajyotkcsu/remind-gpt/assets/154280801/b4701a9c-940f-4709-94a2-fa90d75e282b)


Result: Tasks are produced on separate partitions based on taskType: {wellbeing, social, chores}
Advantages of partitioning tasks is it's simplier to read/process tasks parallelly from each partition with offset values.

## Getting Started
To start using RemindGPT, follow these simple steps:
1. Install docker locally.
2. Implement Kafka docker image by navigating ```https://github.com/conduktor/kafka-stack-docker-compose ``` and choose single zookeeper and broker yml option to get started.
3. Leverage Redis using docker image ``` $ docker run --name some-redis -d redis ```

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

## Next up
In time for OpenAI’s grand opening of the hashtag#GPTStore, we are excited to launch the Ask Instacart GPT, a successor to our ChatGPT Plugin introduced back in May. Far more advanced than its predecessor, the new Instacart GPT leverages every advantage of chatgpt multimodality to talk and see. Now you can Ask Instacart what to make for dinner through text, voice, or by snapping a picture of your fridge or a cookbook recipe. With just a few simple clicks, you can have all the ingredients delivered to your door as fast as within an hour. Give it a try and let us know what you think!


