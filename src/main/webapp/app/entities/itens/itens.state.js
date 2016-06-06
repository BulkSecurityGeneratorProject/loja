(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('itens', {
            parent: 'entity',
            url: '/itens',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.itens.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itens/itens.html',
                    controller: 'ItensController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itens');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('itens-detail', {
            parent: 'entity',
            url: '/itens/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.itens.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/itens/itens-detail.html',
                    controller: 'ItensDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('itens');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Itens', function($stateParams, Itens) {
                    return Itens.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('itens.new', {
            parent: 'itens',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itens/itens-dialog.html',
                    controller: 'ItensDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                qtde: null,
                                valor: null,
                                valorDesconto: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('itens', null, { reload: true });
                }, function() {
                    $state.go('itens');
                });
            }]
        })
        .state('itens.edit', {
            parent: 'itens',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itens/itens-dialog.html',
                    controller: 'ItensDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Itens', function(Itens) {
                            return Itens.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itens', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('itens.delete', {
            parent: 'itens',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/itens/itens-delete-dialog.html',
                    controller: 'ItensDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Itens', function(Itens) {
                            return Itens.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('itens', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
