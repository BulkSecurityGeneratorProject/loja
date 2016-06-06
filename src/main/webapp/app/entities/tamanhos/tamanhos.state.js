(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tamanhos', {
            parent: 'entity',
            url: '/tamanhos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.tamanhos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tamanhos/tamanhos.html',
                    controller: 'TamanhosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tamanhos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tamanhos-detail', {
            parent: 'entity',
            url: '/tamanhos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.tamanhos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tamanhos/tamanhos-detail.html',
                    controller: 'TamanhosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tamanhos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tamanhos', function($stateParams, Tamanhos) {
                    return Tamanhos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('tamanhos.new', {
            parent: 'tamanhos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tamanhos/tamanhos-dialog.html',
                    controller: 'TamanhosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tamanhos', null, { reload: true });
                }, function() {
                    $state.go('tamanhos');
                });
            }]
        })
        .state('tamanhos.edit', {
            parent: 'tamanhos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tamanhos/tamanhos-dialog.html',
                    controller: 'TamanhosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tamanhos', function(Tamanhos) {
                            return Tamanhos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tamanhos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tamanhos.delete', {
            parent: 'tamanhos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tamanhos/tamanhos-delete-dialog.html',
                    controller: 'TamanhosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tamanhos', function(Tamanhos) {
                            return Tamanhos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tamanhos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
