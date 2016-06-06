(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('marcas', {
            parent: 'entity',
            url: '/marcas',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.marcas.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/marcas/marcas.html',
                    controller: 'MarcasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('marcas');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('marcas-detail', {
            parent: 'entity',
            url: '/marcas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.marcas.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/marcas/marcas-detail.html',
                    controller: 'MarcasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('marcas');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Marcas', function($stateParams, Marcas) {
                    return Marcas.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('marcas.new', {
            parent: 'marcas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marcas/marcas-dialog.html',
                    controller: 'MarcasDialogController',
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
                    $state.go('marcas', null, { reload: true });
                }, function() {
                    $state.go('marcas');
                });
            }]
        })
        .state('marcas.edit', {
            parent: 'marcas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marcas/marcas-dialog.html',
                    controller: 'MarcasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Marcas', function(Marcas) {
                            return Marcas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('marcas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('marcas.delete', {
            parent: 'marcas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/marcas/marcas-delete-dialog.html',
                    controller: 'MarcasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Marcas', function(Marcas) {
                            return Marcas.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('marcas', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
