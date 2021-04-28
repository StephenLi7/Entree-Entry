// set up Express
var express = require('express');
var app = express();

// set up EJS
app.set('view engine', 'ejs');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// import the User class from User.js
//var Person = require('./Person.js');
var User = require ('./User.js');
var Food = require ('./Food.js');
/***************************************/

// route for creating a new person
// this is the action of the "create new person" form
app.use('/create', (req, res) => {
	// construct the Person from the form data which is in the request body
	var tcg = 0;
	var thometown = "";
	var tbio = "";
	if (req.body.caloriegoal) {
		tcg = req.body.caloriegoal;
	}
	if (req.body.hometown) {
		thometown = req.body.hometown
	}
	if (req.body.bio) {
		tbio = req.body.bio
	}
	var newUser = new User ({
		username: req.body.username,
		password: req.body.password,
		hometown: thometown,
		bio: tbio,
		caloriegoal: tcg
	    });
	User.findOne({username: req.body.username}, (err, user) => {
		if (error) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		}
		if (user) {
			res.type('html').status(200);
		    res.write('uh oh: user already exists');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		}
	});
	// save the person to the database
	newUser.save( (err) => { 
		if (err) {
		    res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.render('created', {user : newUser});
		}
	    } ); 
    }
    );
//add food
app.use('/addFood', (req, res) => {
	console.log(req.body.foodname);
	var foodName1 = req.body.foodname;
	var company1 = "";
	var calories1 = 0;
	var fat1 = 0;
	var protein1 = 0;
	var sugar1 = 0;
	var salt1 = 0;
	var fiber1 = 0;
	var carbs1 = 0;
	if (req.body.company) {
		company1 = req.body.company;
	}
	if (req.body.calories) {
		calories1 = Number(req.body.calories);
	}
	if (req.body.fat) {
		fat1 = Number(req.body.fat);
	}
	if (req.body.protein) {
		protein1 = Number(req.body.protein);
	}
	if (req.body.sugar) {
		sugar1 = Number(req.body.sugar);
	}
	if (req.body.salt) {
		salt1 = Number(req.body.salt);
	}
	if (req.body.fiber) {
		fiber1 = Number(req.body.fiber);
	}
	if (req.body.carbs) {
		carbs1 = Number(req.body.carbs);
	}
	var newFood = new Food ({
		foodName: foodName1,
	    company: company1,
	    calories: calories1,
	    fat: fat1,
	    protein: protein1,
	    sugar: sugar1,
	    fiber: fiber1,
	    salt: salt1,
	    carbs: carbs1
	    });
	// Food.findOne({foodName: req.body.foodname}, (err, food) => {
	// 	if (error) {
	// 		res.type('html').status(200);
	// 	    res.write('uh oh: ' + err);
	// 	    res.write("<a href='/'>Go Home</a>");
	// 	    res.end();
	// 	}
	// 	if (food) {
	// 		res.type('html').status(200);
	// 	    res.write('uh oh: food already exists');
	// 	    res.write("<a href='/'>Go Home</a>");
	// 	    res.end();
	// 	}
	// });
	// save the person to the database
	newFood.save( (err) => { 
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.type('html');
		    res.write('Food added successfully: ');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		}
	    } ); 
    }
    );

// route for updating a new person
// this is the action of the "create new person" form
app.use('/change', (req, res) => {
	var un = req.body.username2
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: user does not exist');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
				if (req.body.caloriegoal2) {
					user.caloriegoal = Number(req.body.caloriegoal2);
				}
				if (req.body.newusername2) {
					user.username = req.body.newusername2;
				}
				if (req.body.hometown2) {
					user.hometown = req.body.hometown2;
				}
				if (req.body.bio2) {
					user.bio = req.body.bio2;
				}
				if (req.body.password2) {
					user.password = req.body.password2;
				}
				user.save( (err) => {
					if (err) {
						res.send('error saving');
					} else {
						res.render('changed', {user : user});
					}
				});
		}

		});
	});

//route for deleting users
app.use('/remove-user', (req,res) => {
	var user = req.body.deleteUser;
	var myquery = { username: user };
	// find a specific User in the database
	User.deleteOne(myquery, function(err, user) {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: user does not exist');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
			res.render('delete', {user: user})
		}
	});
});

//route for deleting food
app.use('/remove-food-object', (req,res) => {
	var f = req.body.deleteFood;
	var myquery = { foodName: f };
	// find a specific User in the database
	Food.deleteOne(myquery, function(err, food) {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!food) {
			res.type('html').status(200);
		    res.write('uh oh: food does not exist');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
			res.render('deletefood', {food: food})
		}
	});
});

//route for deleting users
app.use('/apiremove-user', (req,res) => {
	var user = req.query.deleteUser;
	var myquery = { username: user };
	// find a specific User in the database
	User.deleteOne(myquery, function(err, user) {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			console.log(myquery);
			res.send('user does not exist');
		} else {
			res.render('delete', {user: user})
		}
	});
});

// route for showing all the people
app.use('/all', (req, res) => {
    
	// find all the Person objects in the database
	User.find( {}, (err, users) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		   	res.write("<a href='/'>Go Home</a>");
		}
		else {
		    if (users.length == 0) {
			res.type('html').status(200);
			res.write('There are no users');
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    res.render('all', { users: users });

		}
	    }).sort({ 'username': 'asc' }); // this sorts them BEFORE rendering the results
    });

// route for showing all the foods
app.use('/allfoods', (req, res) => {
    
	// find all the Person objects in the database
	Food.find( {}, (err, foods) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		   	res.write("<a href='/'>Go Home</a>");
		}
		else {
		    if (foods.length == 0) {
			res.type('html').status(200);
			res.write('There are no foods');
			res.write("<a href='/'>Go Home</a>");
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    res.render('allfoods', { foods: foods });

		}
	    }).sort({ 'foodName': 'asc' }); // this sorts them BEFORE rendering the results
    });

// route for showing all the foods
app.use('/apigetallfoods', (req, res) => {
    
	// find all the Person objects in the database
	Food.find( {}, (err, foods) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		   	res.write("<a href='/'>Go Home</a>");
		}
		else {
		    if (foods.length == 0) {
			res.type('html').status(200);
			res.write('There are no foods');
			res.write("<a href='/'>Go Home</a>");
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    res.json(foods);;

		}
	    }).sort({ 'foodName': 'asc' }); // this sorts them BEFORE rendering the results
    });

//route for showing friends
app.use('/getfriendinfo', (req, res) => {
    var un = req.body.username;
	// find all the Person objects in the database
	User.findOne( {username: un}, (err, user) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
		    
		    // use EJS to show all the people

		    res.render('friendinfo', { username: user.username, friends: user.friends, requests: user.friendRequests });

		}
	    }).sort({ 'username': 'asc' }); // this sorts them BEFORE rendering the results
    });

//route for showing food
app.use('/getfoodinfo', (req, res) => {
    var un = req.body.username;
	// find all the Person objects in the database
	User.findOne( {username: un}, (err, user) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
		    
		    // use EJS to show all the people

		    res.render('foodinfo', { username: user.username, foodEntries: user.foodEntries});

		}
	    }).sort({ 'username': 'asc' }); // this sorts them BEFORE rendering the results
    });

// route for accessing data via the web api
// to use this, make a request for /api to get an array of all Person objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {
	console.log("LOOKING FOR SOMETHING?");

	// construct the query object
	var queryObject = {};
	if (req.query.username) {
	    // if there's a name in the query parameter, use it here
	    queryObject = { "username" : req.query.username };
	}
    
	User.find( queryObject, (err, users) => {
		console.log(users);
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		}
		else if (users.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		}
		else if (users.length == 1 ) {
		    var user = users[0];
		    // send back a single JSON object
		    res.json( { "username" : user.username , 
		    	"password" : user.password ,
		    	"hometown" : user.hometown ,
		    	"bio" : user.bio ,
		    	"caloriegoal" : user.caloriegoal } );
		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    users.forEach( (user) => {
			    returnArray.push( { "username" : user.username , 
		    	"password" : user.password ,
		    	"hometown" : user.hometown ,
		    	"bio" : user.bio ,
		    	"caloriegoal" : user.caloriegoal } );
			});
		    // send it back as JSON Array
		    res.json(returnArray); 
		}
		
	    });
    });

app.use('/apifoods', (req, res) => {
	console.log("LOOKING FOR SOMETHING?");

	// construct the query object
	var queryObject = {};
	if (req.query.foodName) {
	    // if there's a name in the query parameter, use it here
	    queryObject = { "foodName" : req.query.foodName };
	}
    
	Food.find( queryObject, (err, foods) => {
		console.log(foods);
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		}
		else if (foods.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		}
		else if (foods.length == 1 ) {
		    var food = foods[0];
		    // send back a single JSON object
		    res.json( { "foodName" : food.foodName , 
		    	"company": food.company,
			    "calories": food.calories,
			    "fat": food.fat,
			    "protein": food.protein,
			    "sugar": food.sugar,
			    "fiber": food.fiber,
			    "salt": food.salt,
			    "carbs": food.carbs } );
		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    users.forEach( (user) => {
			    returnArray.push( { "foodName" : food.foodName , 
		    	"company": food.company,
			    "calories": food.calories,
			    "fat": food.fat,
			    "protein": food.protein,
			    "sugar": food.sugar,
			    "fiber": food.fiber,
			    "salt": food.salt,
			    "carbs": food.carbs } );
			});
		    // send it back as JSON Array
		    res.json(returnArray); 
		}
		
	    });
    });

// API route for creating a new person
// this is the action of the "create new person" form
app.use('/apicreate', (req, res) => {
	var newUser = new User ({
		username: req.query.username,
		password: req.query.password,
		hometown: '',
		bio: '',
		caloriegoal: 0
	    });
	// save the person to the database
	newUser.save( (err) => { 
		if (err) {
		    res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.end();
		} else {
			res.json(newUser);
		}} ); 
    });

// route for updating a new person
// this is the action of the "create new person" form
app.use('/apichange', (req, res) => {
	var un = req.query.username2
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				if (req.query.caloriegoal2) {
					user.caloriegoal = Number(req.query.caloriegoal2);
				}
				if (req.query.newusername2) {
					user.username = req.query.newusername2;
				}
				if (req.query.hometown2) {
					user.hometown = req.query.hometown2;
				}
				if (req.query.bio2) {
					user.bio = req.query.bio2;
				}
				if (req.query.password2) {
					user.password = req.query.password2;
				}
				user.save( (err) => {
					if (err) {
						res.json({username : ""})
					} else {
						res.json(user);
					}
				});
		}

		});
	});

// route for updating a new person
// this is the action of the "create new person" form
app.use('/addfriend', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				if (req.query.friendName) {
					user.friends.push({friendName : req.query.friendName});
				}
				user.save( (err) => {
					if (err) {
						res.json({username : ""})
					} else {
						res.json(user);
					}
				});
		}

		});
	});

app.use('/getfriends', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
			res.json(user.friends);
		}

		});
	});


app.use('/addrequest', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				if (req.query.requestName) {
					user.friendRequests.push({requestName : req.query.requestName, 
						rstatus : req.query.rstatus});
				}
				user.save( (err) => {
					if (err) {
						res.json({username : ""})
					} else {
						res.json(user);
					}
				});
		}

		});
	});

app.use('/getrequests', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				res.json(user.friendRequests);
		}

		});
	});

app.use('/getentries', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				res.json(user.foodEntries);
		}

		});
	});

app.use('/addentry', (req, res) => {
	var un = req.query.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
				if (req.query.foodName && req.query.foodCalories && req.query.date) {
					user.foodEntries.push({date : req.query.date, 
						foodName : req.query.foodName, foodCalories: Number(req.query.foodCalories)});
				}
				user.save( (err) => {
					if (err) {
						res.json({username : ""})
					} else {
						res.json(user);
					}
				});
		}

		});
	});

//returns "" if doesnt work
app.use('/removerequest', (req, res) => {
	var un = req.query.username
	User.findOneAndUpdate( {username: un }, {$pull: {friendRequests:{requestName : req.query.requestName}}}, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
			res.json(user);
		}

		});
	});

//for api usage by android
app.use('/removefriend', (req, res) => {
	var un = req.query.username
	User.findOneAndUpdate( {username: un }, {$pull: {friends:{friendName : req.query.friendName}}}, (err, user) => {
		if (err) {
			res.json({username : ""})
		} else if (!user) {
			res.json({username : ""})
		} else {
			res.json(user);
		}

		});
	});

//for webapp usage
app.use('/remove-friend', (req, res) => {
	var un = req.body.username
	User.findOneAndUpdate( {username: un }, {$pull: {friends:{friendName : req.body.deleteFriend}}}, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
			User.findOneAndUpdate( {username: req.body.deleteFriend }, {$pull: {friends:{friendName : un}}}, (err, user2) => {
				if (err) {
					res.type('html').status(200);
				    res.write('uh oh: ' + err);
				    res.write("<a href='/'>Go Home</a>");
				    res.end();
				} else if (!user2) {
					res.type('html').status(200);
				    res.write('uh oh query not found');
				    res.write("<a href='/'>Go Home</a>");
				    res.end();
				} else {
					User.findOne({username: un}, (err, usernew) => {
						res.render('friendinfo', { username: usernew.username, 
						friends: usernew.friends, requests: usernew.friendRequests });
					});
				}
			});
		}
	});
});

//for webapp usage
app.use('/remove-request', (req, res) => {
	var un = req.body.username
	User.findOneAndUpdate( {username: un }, {$pull: {friendRequests:{requestName : req.body.deleteRequest}}}, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
			User.findOneAndUpdate( {username: req.body.deleteRequest }, {$pull: {friendRequests:{requestName : un}}}, (err, user2) => {
				if (err) {
					res.type('html').status(200);
				    res.write('uh oh: ' + err);
				    res.write("<a href='/'>Go Home</a>");
				    res.end();
				} else if (!user2) {
					res.type('html').status(200);
				    res.write('uh oh: user not found');
				    res.write("<a href='/'>Go Home</a>");
				    res.end();
				} else {
					User.findOne({username: un}, (err, usernew) => {
						res.render('friendinfo', { username: usernew.username, 
						friends: usernew.friends, requests: usernew.friendRequests });
					});
				}
			});
		}
	});
});

//for webapp usage
app.use('/add-friend', (req, res) => {
	var un = req.body.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
				if (req.body.friendAdd) {
					user.friends.push({friendName : req.body.friendAdd});
				}
				user.save( (err) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();
					}
				});
				User.findOne({username : req.body.friendAdd}, (err, user2) =>{
					user2.friends.push({friendName: un});
					user2.save();
					User.findOne({username: un}, (err, user3) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();
					}
					res.render('friendinfo', { username: user3.username, 
						friends: user3.friends, requests: user3.friendRequests });
				})
				});	
		}
	});
});


//for webapp usage
app.use('/add-request', (req, res) => {
	var un = req.body.username
	console.log(req.body.requestAdd);
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
				if (req.body.requestAdd) {
					user.friendRequests.push({requestName : req.body.requestAdd, rstatus: "true"});
				}
				user.save( (err) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();
					}
				});
				User.findOne({username : req.body.requestAdd}, (err, user2) =>{
					user2.friendRequests.push({requestName: un, rstatus: "false"});
					user2.save();
					User.findOne({username: un}, (err, user3) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();
					}
					res.render('friendinfo', { username: user3.username, 
						friends: user3.friends, requests: user3.friendRequests });
				})
				});	
		}
	});
});

//for webapp usage
app.use('/add-food', (req, res) => {
	var un = req.body.username
	User.findOne( {username: un }, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
				if (req.body.addDate && req.body.addName && req.body.addCal) {
					user.foodEntries.push({date : req.body.addDate, foodName: req.body.addName, foodCalories: req.body.addCal});
				}
				user.save( (err) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();					}
					User.findOne({username: un}, (err, user3) => {
					if (err) {
						res.type('html').status(200);
					    res.write('uh oh: ' + err);
					    res.write("<a href='/'>Go Home</a>");
					    res.end();
					}
					res.render('foodinfo', { username: user3.username, 
						foodEntries: user3.foodEntries });
					})
				});

		}
	});
});
//for webapp usage
app.use('/remove-food', (req, res) => {
	var un = req.body.username
	User.findOneAndUpdate( {username: un }, {$pull: {foodEntries:{date : req.body.deleteDate, foodName: req.body.deleteName, foodCalories: req.body.deleteCal}}}, (err, user) => {
		if (err) {
			res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else if (!user) {
			res.type('html').status(200);
		    res.write('uh oh: query not found');
		    res.write("<a href='/'>Go Home</a>");
		    res.end();
		} else {
			User.findOne({username: un}, (err, usernew) => {
				res.render('foodinfo', { username: usernew.username, 
						foodEntries: usernew.foodEntries });
			});
		}
	});
});


/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/home.html'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });



app.use('/test', (req, res) => {
    // create a JSON object
    var data = { 'message' : 'It works!' };
    // send it back
    res.json(data);
});
