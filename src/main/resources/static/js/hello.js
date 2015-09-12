angular
		.module('hr', [ 'ngRoute' ])
		.config(
				function($routeProvider, $httpProvider) {

					$routeProvider.when('/', {
						templateUrl : 'home.html',
						controller : 'home'
					}).when('/login', {
						templateUrl : 'login.html',
						controller : 'navigation'
					}).when('/add',{
						templateUrl : 'add.html',
						controller : 'add'
					}).when('/search',{
						templateUrl : 'search.html',
						controller : 'search'
					}).when('/edit/:id',{
						templateUrl : 'edit.html',
						controller : 'edit'
					}).otherwise('/');
					$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

				})
		.controller('home', function($scope, $http) {
			$http.get('/user/').success(function(data) {
				$scope.greeting = data;
			})
		}).controller('search', function($scope, $http, $location) {
			$scope.searchEmployee = function() {
				var searchText = $scope.searchText;
				$http
						.get(
								"/employee/search/findByLastNameContaining?name="
										+ searchText)
						.then(
								function(result) {
									if (result && result.data && result.data._embedded
											&& result.data._embedded.employee) {
										$scope.searchResult = result.data._embedded.employee;
									} else {
										$scope.searchResult = {};
									}
								});
			};
			$scope.remove = function(index){
				var remove_link = $scope.searchResult[index]._links.self.href;
				$http.delete(remove_link);
				$scope.searchResult.splice(index,1);
			};
			$scope.edit = function(index){
				var emp = $scope.searchResult[index];
				$location.path("/edit/"+emp.identityString);
			};
			$scope.searchText = '';
			$scope.searchResult = {};
		}).controller('edit', function($scope,$routeParams,$http,$location) {
			$scope.save = function()
			{
				$http.patch($scope.update_link,$scope.employee).success(function(){
					
				});
				$location.path("/search");
			};
			$http.get('/employee/'+$routeParams.id).success(function(data){
				$scope.employee = {};
				$scope.employee.firstName = data.firstName;
				$scope.employee.lastName = data.lastName;
				$scope.employee.role = data.role;
				$scope.employee.location = data.location;
				$scope.employee.email = data.email;
				$scope.employee.phone = data.phone;
				$scope.update_link = data._links.self.href;
			});
		}).controller('add', function($scope,$http,$location) {
			$scope.save = function()
			{
				$http.post("/employee", $scope.employee).success(function() {
					$location.path("/add");
					$scope.employee = createEmployee();
					$scope.error = false;
					$scope.success = true;
				}).error(function() {
					$location.path("/add");
					$scope.error = true;
					$scope.success = false;
				});
			};
			var createEmployee = function() {
				return {
					firstName : '',
					lastName : '',
					password : '',
					role : 'USER',
					email : '',
					location :'',
					phone :''
				};
			};
			$scope.employee = createEmployee();

		})
		.controller(
				'navigation',
				function($rootScope, $scope, $http, $location, $route) {

					var authenticate = function(credentials, callback) {

						var headers = credentials ? {
							authorization : "Basic "
									+ btoa(credentials.username + ":"
											+ credentials.password)
						} : {};

						$http
								.get('user', {
									headers : headers
								})
								.success(
										function(data) {
											if (data.name) {
												$rootScope.authenticated = true;
												$rootScope.admin = data.authorities
														.filter(function(obj) {
															return obj.authority == 'ADMIN';
														}).length > 0;
												$rootScope.user = data.authorities
												.filter(function(obj) {
													return obj.authority == 'USER';
												}).length > 0;
												$rootScope.authorities = data.authorities;
											} else {
												$rootScope.authenticated = false;
												$rootScope.admin = false;
												$rootScope.user = false;
											}
											callback && callback();
										}).error(function() {
									$rootScope.authenticated = false;
									callback && callback();
								});

					}
					
					$scope.login = function() {
						authenticate($scope.credentials, function() {
							if ($rootScope.authenticated) {
								$location.path("/");
								$scope.error = false;
							} else {
								$location.path("/login");
								$scope.error = true;
							}
						});
					};
					$scope.logout = function() {
						$http.post('/logout', {});
						$rootScope.authenticated = false;
						$location.path("/");
						$route.reload();
					};
					authenticate();
				});