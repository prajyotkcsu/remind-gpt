# RemindGPT

## Overview
RemindGPT is a simple yet effective task management tool designed to help you stay organized and focused throughout your day. Its core functionality revolves around loading your wishlist (an array of tasks) into the ChatGPT engine through the OpenAI API. This process transforms the tasks into tuples of (task, task_type) using prompt engineering, which are then fed into corresponding partitions on a Kafka topic. Tasks across Kafka partitions are retrieved and stored inside a Priority Queue to sort and return them based on their priorities (high, medium, low), which are set by the user. The Priority Queue is ultimately returned to the user. 

At the moment, the app supports five major categories of tasks, enumerated as Fitness, Social, Self-Development, Responsibilities, and Work.

Example POST request from the user:
```
{
  tasks: ["take a walk", "commit git code", "call a friend", "attend tonight's pizza party", "call Amazon customer service", "take out trash"]
}
{
  priorities:["medium", "high", "medium", "medium", "low", "low"]
}

```

Example HTTP 200 response from the ChatGPT API (requires some parsing to obtain this format):
```
{
  gptTasks: [
    {
      task: "take a walk",
      category: "fitness"
    },
    {
      task: "commit git code at 9 pm tonight",
      category: "work"
    },
    {
      task: "call a friend",
      category: "social"
    },
    {
      task: "attend tonight's pizza party",
      category: "social"
    },
    {
      task: "call Amazon customer service",
      category: "responsibility"
    },
    {
      task: "take out trash",
      category: "responsibility"
    }
  ]
}

```

returns items form the priority queue every 2 hours based on priority.
```
{
  todoList: ["commit code", "take a walk", "take out trash", "attend tonight's pizza party"]
}
```


## Features
- **Smart Task Reminders**: Rather than overwhelming you with a list of all remaining tasks, RemindGPT intelligently considers the time you have before your break. It prompts you to input the available time, and then suggests tasks that can be achieved within that timeframe.

- **Personalized Assistance**: Think of RemindGPT as your friend. It assists you in making the most out of your break time by recommending tasks that align with the time you have available.

## System Design: Bird's eye view of the app

![Untitled Diagram drawio](https://github.com/prajyotkcsu/remind-gpt/assets/154280801/ffa8bbc2-ae00-4243-bdb7-e225f23b8c6a)




## Snapshots of apis
1) Send request to the app
 ```
   localhost:8084/reminder/api/task/produce
```
![image](https://github.com/prajyotkcsu/remind-gpt/assets/154280801/b4701a9c-940f-4709-94a2-fa90d75e282b)

Result: Tasks are produced on separate partitions based on taskType: {wellbeing, social, chores}
Advantages of partitioning tasks is it's simplier to read/process tasks parallelly from each partition with offset values.


## Getting Started
To start using RemindGPT, follow these simple steps:
1. Clone the repository: `git clone https://github.com/your-username/RemindGPT.git`
2. Navigate to the project directory: `cd RemindGPT`
3. Install docker locally.
4. Implement Kafka using docker container found at ```src/main/resources/kafka-compose.yml```
5. Leverage Redis using docker container found at ```src/main/resources/redis-compose.yml```

## Usage
1. Launch the application.
2. Enter your wishlist for the day.
3. When prompted, input the time you have for your break.
4. Receive personalized task recommendations.
5. Enjoy a productive break with RemindGPT's assistance.

## Next up
In time for OpenAI’s grand opening of the hashtag#GPTStore, we are excited to launch the Ask Instacart GPT, a successor to our ChatGPT Plugin introduced back in May. Far more advanced than its predecessor, the new Instacart GPT leverages every advantage of chatgpt multimodality to talk and see. Now you can Ask Instacart what to make for dinner through text, voice, or by snapping a picture of your fridge or a cookbook recipe. With just a few simple clicks, you can have all the ingredients delivered to your door as fast as within an hour. Give it a try and let us know what you think!


