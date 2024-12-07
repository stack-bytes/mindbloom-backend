import pandas as pd
import joblib

columns = [
    "Sadness", "Euphoric", "Exhausted", "Sleep_dissorder", "Mood_Swing", 
    "Suicidal_thoughts", "Anorxia", "Authority_Respect", "Try-Explanation", 
    "Aggressive_Response", "Ignore_&_Move-On", "Nervous_Break-down", 
    "Admit_Mistakes", "Overthinking", "Sexual_Activity", "Concentration", 
    "Optimisim"
]

illness = {
    0: "Depression",
    1: "Normal",
    2: "Bipolar Type-1",
    3: "Bipolar Type-2"
}

model = joblib.load('model.joblib')

preprocessor = joblib.load('preprocessor.joblib')

def modelService(data) :
    row = [data[column] for column in columns]
    dataset = pd.DataFrame([row], columns=columns)
    dataset = preprocessor.transform(dataset)
    prediction = model.predict(dataset)
    return illness[prediction[0]]