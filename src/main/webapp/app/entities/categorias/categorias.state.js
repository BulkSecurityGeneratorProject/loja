(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('categorias', {
            parent: 'entity',
            url: '/categorias',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.categorias.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorias/categorias.html',
                    controller: 'CategoriasController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorias');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('categorias-detail', {
            parent: 'entity',
            url: '/categorias/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.categorias.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorias/categorias-detail.html',
                    controller: 'CategoriasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorias');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Categorias', function($stateParams, Categorias) {
                    return Categorias.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('categorias.new', {
            parent: 'categorias',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorias/categorias-dialog.html',
                    controller: 'CategoriasDialogController',
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
                    $state.go('categorias', null, { reload: true });
                }, function() {
                    $state.go('categorias');
                });
            }]
        })
        .state('categorias.edit', {
            parent: 'categorias',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorias/categorias-dialog.html',
                    controller: 'CategoriasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorias', function(Categorias) {
                            return Categorias.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorias.delete', {
            parent: 'categorias',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorias/categorias-delete-dialog.html',
                    controller: 'CategoriasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Categorias', function(Categorias) {
                            return Categorias.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorias', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
