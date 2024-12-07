import pandas as pd
import joblib
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import accuracy_score
from sklearn.compose import ColumnTransformer


dataset = pd.read_csv('Dataset-Mental-Disorders.csv')

# Options for the complete display of the dataset
pd.set_option('display.max_columns', None)
pd.set_option('display.max_colwidth', None)
pd.set_option('display.expand_frame_repr', False)

dataset.columns = dataset.columns.str.replace(" ", "_")

dataset = dataset.drop('Patient_Number', axis=1)

LABELS = ['Depression', 'Normal', 'Bipolar Type-1', 'Bipolar Type-2']

label2id = dict(zip(LABELS, range(len(LABELS))))

dataset['Expert_Diagnose'] = dataset['Expert_Diagnose'].map(label2id)

X = dataset.drop('Expert_Diagnose', axis=1)  # Assuming 'Expert_Diagnose' is the target variable
y = dataset['Expert_Diagnose']

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3,random_state=42)

categorical_variables = X_train.select_dtypes(include = ['object','category']).columns.to_list()

# Define the preprocessing steps using ColumnTransformer
preprocessor = ColumnTransformer([
    ('ohe', OneHotEncoder(drop='first', sparse_output=False, handle_unknown='ignore'), categorical_variables)
], remainder='passthrough')

# Fit and transform the training data
X_train_prep = preprocessor.fit_transform(X_train)

print(X_test)

# Transform the testing data
X_test_prep = preprocessor.transform(X_test)

# Initialize the model

model = RandomForestClassifier()

model.fit(X_train_prep, y_train)

y_pred = model.predict(X_test_prep)

accuracy = accuracy_score(y_test, y_pred)

print(f"Accuracy: {accuracy:.2f}")

print(f"Cross Validation Score: {cross_val_score(model, X_train_prep, y_train, cv=5).mean():.2f}")

# Save the model

joblib.dump(model, 'model.joblib')

# Save the preprocessor

joblib.dump(preprocessor, 'preprocessor.joblib')