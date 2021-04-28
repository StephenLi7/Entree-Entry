var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb+srv://goMongo:npORntN3wrhQnHMm@entreeentrycluster-ynh8q.mongodb.net/test?retryWrites=true&w=majority');

var Schema = mongoose.Schema;

var foodSchema = new Schema({
    foodName: {type: String, required: true, unique: true},
    company: String,
    calories: Number,
    fat: Number,
    protein: Number,
    sugar: Number,
    fiber: Number,
    salt: Number,
    carbs: Number
    });

// export personSchema as a class called User
module.exports = mongoose.model('Food', foodSchema);

foodSchema.methods.standardizeName = function() {
    this.foodName = this.foodName.toLowerCase();
    return this.foodName;
}
