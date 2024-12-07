import pandas as pd
import joblib

# Options for the complete display of the dataset
pd.set_option('display.max_columns', None)
pd.set_option('display.max_colwidth', None)
pd.set_option('display.expand_frame_repr', False)

columns = [
    "Sadness", "Euphoric", "Exhausted", "Sleep_dissorder", "Mood_Swing", 
    "Suicidal_thoughts", "Anorxia", "Authority_Respect", "Try-Explanation", 
    "Aggressive_Response", "Ignore_&_Move-On", "Nervous_Break-down", 
    "Admit_Mistakes", "Overthinking", "Sexual_Activity", "Concentration", 
    "Optimisim"
]

model = joblib.load('model.joblib')

preprocessor = joblib.load('preprocessor.joblib')

def modelService(data) :
    row = [data[column] for column in columns]
    dataset = pd.DataFrame([row], columns=columns)
    dataset['Suicidal_thoughts'] = dataset['Suicidal_thoughts'].replace('YES ', 'YES')

    yes_no_cols = ['Mood_Swing', 'Suicidal_thoughts', 'Anorxia', 'Authority_Respect', 'Try-Explanation', 'Aggressive_Response', 'Ignore_&_Move-On', 'Nervous_Break-down', 'Admit_Mistakes', 'Overthinking']

    for col in yes_no_cols:
        dataset[col] = dataset[col].apply(lambda x: x if x in ['YES', 'NO'] else 'NO')

    for col in yes_no_cols:
        dataset[col] = dataset[col].map({'YES': 1, 'NO': 0}).astype(int)
    dataset = preprocessor.transform(dataset)
    prediction = model.predict(dataset)
    return prediction[0]