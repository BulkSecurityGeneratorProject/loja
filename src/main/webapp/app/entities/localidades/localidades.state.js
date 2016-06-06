(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('localidades', {
            parent: 'entity',
            url: '/localidades',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.localidades.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localidades/localidades.html',
                    controller: 'LocalidadesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localidades');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('localidades-detail', {
            parent: 'entity',
            url: '/localidades/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.localidades.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localidades/localidades-detail.html',
                    controller: 'LocalidadesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localidades');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Localidades', function($stateParams, Localidades) {
                    return Localidades.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('localidades.new', {
            parent: 'localidades',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localidades/localidades-dialog.html',
                    controller: 'LocalidadesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                endereco: null,
                                cep: null,
                                bairro: null,
                                cidade: null,
                                uF: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('localidades', null, { reload: true });
                }, function() {
                    $state.go('localidades');
                });
            }]
        })
        .state('localidades.edit', {
            parent: 'localidades',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localidades/localidades-dialog.html',
                    controller: 'LocalidadesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Localidades', function(Localidades) {
                            return Localidades.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('localidades', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('localidades.delete', {
            parent: 'localidades',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localidades/localidades-delete-dialog.html',
                    controller: 'LocalidadesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Localidades', function(Localidades) {
                            return Localidades.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('localidades', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
