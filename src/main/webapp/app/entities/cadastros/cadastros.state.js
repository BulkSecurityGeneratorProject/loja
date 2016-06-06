(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cadastros', {
            parent: 'entity',
            url: '/cadastros',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.cadastros.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cadastros/cadastros.html',
                    controller: 'CadastrosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cadastros');
                    $translatePartialLoader.addPart('tipoPessoa');
                    $translatePartialLoader.addPart('tipoCadastro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cadastros-detail', {
            parent: 'entity',
            url: '/cadastros/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.cadastros.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cadastros/cadastros-detail.html',
                    controller: 'CadastrosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cadastros');
                    $translatePartialLoader.addPart('tipoPessoa');
                    $translatePartialLoader.addPart('tipoCadastro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cadastros', function($stateParams, Cadastros) {
                    return Cadastros.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('cadastros.new', {
            parent: 'cadastros',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cadastros/cadastros-dialog.html',
                    controller: 'CadastrosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                tipoPessoa: null,
                                tipoCadastro: null,
                                email: null,
                                observacoes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cadastros', null, { reload: true });
                }, function() {
                    $state.go('cadastros');
                });
            }]
        })
        .state('cadastros.edit', {
            parent: 'cadastros',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cadastros/cadastros-dialog.html',
                    controller: 'CadastrosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cadastros', function(Cadastros) {
                            return Cadastros.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cadastros', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cadastros.delete', {
            parent: 'cadastros',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cadastros/cadastros-delete-dialog.html',
                    controller: 'CadastrosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cadastros', function(Cadastros) {
                            return Cadastros.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cadastros', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
