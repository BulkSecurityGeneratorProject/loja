(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cores', {
            parent: 'entity',
            url: '/cores',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.cores.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cores/cores.html',
                    controller: 'CoresController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cores');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cores-detail', {
            parent: 'entity',
            url: '/cores/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.cores.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cores/cores-detail.html',
                    controller: 'CoresDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cores');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cores', function($stateParams, Cores) {
                    return Cores.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('cores.new', {
            parent: 'cores',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cores/cores-dialog.html',
                    controller: 'CoresDialogController',
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
                    $state.go('cores', null, { reload: true });
                }, function() {
                    $state.go('cores');
                });
            }]
        })
        .state('cores.edit', {
            parent: 'cores',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cores/cores-dialog.html',
                    controller: 'CoresDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cores', function(Cores) {
                            return Cores.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cores', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cores.delete', {
            parent: 'cores',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cores/cores-delete-dialog.html',
                    controller: 'CoresDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cores', function(Cores) {
                            return Cores.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cores', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
