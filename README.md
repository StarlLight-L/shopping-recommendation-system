# AI-powered E-commerce Recommendation Platform 

## Demo

This application is deployed on Vercel. Please check it out  [here]().

See admin dashboard [demo]().

## Introduction

This project is an AI-powered shopping mall system designed to provide personalized product recommendations based on user behavior, device preferences, browsing history, favorites, cart actions, and purchase records.

The system combines:
- Hybrid recommendation strategies
- Real-time behavior scoring
- DeepFM ranking model
- Exploration recommendation
- Similarity recall
- Device-aware recommendation
- User preference modeling

## Tech Stack

### Frontend
- Vue3
- Vuex
- Vue Router
- Element Plus
- Axios

### Backend
- Spring Boot 
- Java 
- MySQL 
- MyBatis 

### Recommendation System
- Python
- FastAPI
- Hybrid Recommendation
- DeepFM
- Collaborative Filtering
- Recall & Ranking
- Real-time User Behavior Scoring

### Deployment
- Docker
- GitHub

## Recommendation System

The recommendation system adopts a hybrid recommendation architecture:

### Recall Stage
- Popular item recall
- Similarity recall
- Exploration recall
- New item recall

### Ranking Stage
A DeepFM model is used for ranking candidate items based on:
- User behavior
- Click history
- Favorites
- Cart interactions
- Purchase records
- Device information
- Brand preference
- Category preference

### Real-time Feedback
The system dynamically adjusts recommendation scores according to:
- Positive feedback
- Negative feedback
- Device usage
- User interaction frequency

## Installation

### Backend

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```

### Frontend

```
cd frontend
npm install
npm run dev
```

## Database

Main tables: 

- users 
- products 
- order
- order_item
- devices
- favorite
- cart_items
- user_search_history
- read_history
- (user_reviews) not finish yet

You can also design yourr own database schema or extend existing tables to improve recommendation performance and data processing efficiency. 

The database schema is designed to support personalized recommendation, user behavior analysis, and scalable recommendation model training. 

## Future Improvements

- Real-time recommendation with Apache Flink
- Multi-modal recommendation
- LLM-based shopping assistant
- Better cold-start strategy
- Online A/B testing
- Distributed recommendation architecture
- User review and rating system 
- Review based recommendation 
- NLP-based review sentiment analysis 

## Author

Li Xinman (Mandy)

Master of Information Technology  
University of New South Wales