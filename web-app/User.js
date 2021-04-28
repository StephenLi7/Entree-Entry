var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb+srv://goMongo:npORntN3wrhQnHMm@entreeentrycluster-ynh8q.mongodb.net/test?retryWrites=true&w=majority');

var Schema = mongoose.Schema;

var userSchema = new Schema({
    username: {type: String, required: true, unique: true},
    password: {type: String, required: true},
    hometown: String,
    bio: String,
    caloriegoal: Number,
    foodEntries: [{date: String, foodName: String, foodCalories: Number }],
    friends: [{friendName: String}],
    friendRequests: [{requestName: String, rstatus: String}]
    });

// export personSchema as a class called User
module.exports = mongoose.model('User', userSchema);

userSchema.methods.standardizeName = function() {
    this.username = this.username.toLowerCase();
    return this.username;
}
