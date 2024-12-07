from flask import Flask, request,jsonify, make_response
import modelService

app = Flask(__name__)

@app.route("/predict" , methods=["POST"])
def predict():
    if(request.is_json):
        data = request.get_json()
        result = modelService.modelService(data)
        return make_response(jsonify(result), 200)
    return make_response(jsonify({"message": "Request body must be JSON"}), 400)

if __name__ == "__main__":
  app.run(host="0.0.0.0", port=5000)