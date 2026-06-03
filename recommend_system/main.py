from fastapi import FastAPI, Query
from fastapi.middleware.cors import CORSMiddleware
from recommend.logic import recommend_for_user
from recommend.ranking.predict import DeepFMPredictor
app = FastAPI()
predictor = DeepFMPredictor()

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],  # Default Vue development port, make sure it matches your local setup.
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
@app.get("/recommend")
def recommend(user_id: str):
    try:

        result = recommend_for_user(user_id,predictor)

        return {
            "success": True,
            "user_id": user_id,
            "recommendations": result
        }

    except Exception as e:
        return {
            "success": False,
            "message": str(e)
        }
