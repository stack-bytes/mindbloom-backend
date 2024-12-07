import pandas as pd
import joblib
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import accuracy_score
from sklearn.compose import ColumnTransformer

dataset = pd.read_csv('Dataset-Mental-Disorders.csv')

dataset.columns = dataset.columns.str.replace(" ", "_")

# Options for the complete display of the dataset
pd.set_option('display.max_columns', None)
pd.set_option('display.max_colwidth', None)
pd.set_option('display.expand_frame_repr', False)

dataset = dataset.drop('Patient_Number', axis=1)

dataset['Suicidal_thoughts'] = dataset['Suicidal_thoughts'].replace('YES ', 'YES')

yes_no_cols = ['Mood_Swing', 'Suicidal_thoughts', 'Anorxia', 'Authority_Respect', 'Try-Explanation', 'Aggressive_Response', 'Ignore_&_Move-On', 'Nervous_Break-down', 'Admit_Mistakes', 'Overthinking']

for col in yes_no_cols:
    dataset[col] = dataset[col].map({'YES': 1, 'NO': 0}).astype(int)

X = dataset.drop(columns=['Expert_Diagnose'],axis=1)
y = dataset['Expert_Diagnose']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

encoder = OneHotEncoder(handle_unknown='ignore')
X_train_encoded = encoder.fit_transform(X_train)
X_test_encoded = encoder.transform(X_test)

rf = RandomForestClassifier()
rf.fit(X_train_encoded, y_train)

y_pred = rf.predict(X_test_encoded)

accuracy = accuracy_score(y_test, y_pred)

print(f"Accuracy: {accuracy:.2f}")

print(f"Cross Validation Score: {cross_val_score(rf, X_train_encoded, y_train, cv=5).mean():.2f}")

# joblib.dump(rf, 'model.joblib')

# joblib.dump(encoder, 'preprocessor.joblib')