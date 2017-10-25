(function() {

	var app = angular.module('bankModule',[]);
	
   	app.controller('bankCtrl',['$scope','$http',function($scope,$http) {
   		
   		$scope.transactions=[]; //transactions array
   		$scope.selectedAccountId=null; // selctedAccountId for details 
   		$scope.depositData={}; // data to post to server when deposit/withdraw 
   		$scope.transfertData={};// data to post to server when transfert between accounts 
   		$scope.title= "Bank Kata app"; //App title

   		//Used to retrieve list of accounts, Called when page init.
		$scope.getAccounts=function(){
			$http.get('/rest/accounts-list').then(function(resp){
				$scope.accounts=resp.data.sort(function(a, b){return (a.accountId > b.accountId) ? 1 : ((b.accountId > a.accountId) ? -1 : 0);});;
			});
		},
		
		// Used to retrieve list of transaction for a specific account id
		$scope.retrieveTransactions=function(){
			$scope.selectedAccount = $scope.accounts.filter(acc => acc.accountId==$scope.selectedAccountId)[0];
			$http.get('/rest/transaction-list/'+$scope.selectedAccountId).then(function(resp){
				$scope.transactions=resp.data;
			});
			
		},
		
		// Used to transfert money
		$scope.transfert=function(){
			if($scope.transfertData.selectedAccountTo!=null 
					&& $scope.transfertData.selectedAccountFrom!=null && $scope.transfertData.amountToTransfert){
				var transfertData={
						payerId: $scope.transfertData.selectedAccountFrom,
						payeeId: $scope.transfertData.selectedAccountTo,
						amount : $scope.transfertData.amountToTransfert
					};
				 $http.post('/rest/transfert-amount', transfertData).then(function(resp){
					 	$scope.transfertData={};
						$scope.handlePostRequest(resp.data);
					});
			}else{
				window.alert('choose an account and put an amount '); //Used only for demo purposes 
			}
		},
		
		// Used to withdraw money from a specific account
		$scope.withdraw=function(){
			if($scope.depositData.selectedAccountDw!=null && $scope.depositData.amountToDw!=null){
				var depositData={
						accountId: $scope.depositData.selectedAccountDw,
						amount   : $scope.depositData.amountToDw
					};
				 $http.post('/rest/with-draw-amount', depositData).then(function(resp){
				 		$scope.depositData={};
						$scope.handlePostRequest(resp.data);
					});
			}else{
				window.alert('choose an account and put an amount '); //Used only for demo purposes 
			}
		},
		
		// Used to deposit money for a specific account
		$scope.deposit=function(){
			if($scope.depositData.selectedAccountDw!=null && $scope.depositData.amountToDw!=null){
				var depositData={
						accountId: $scope.depositData.selectedAccountDw,
						amount   : $scope.depositData.amountToDw
					};
				 $http.post('/rest/deposit-amount', depositData).then(function(resp){
					 $scope.depositData={};
					 $scope.handlePostRequest(resp.data);
				 });
			}else{
				window.alert('choose an account and put an amount '); //Used only for demo purposes 
			}
		},
		
		// Used to update data after a post call 
		$scope.handlePostRequest=function(data){
			if(data.status && data.accountDto.accountId===$scope.selectedAccountId ){
				$scope.selectedAccount.balance=data.accountDto.balance;
				$scope.retrieveTransactions();
			}else if(data.status==false){
				window.alert(data.message);
			}
		}
   		
   	}]);
}());
   		