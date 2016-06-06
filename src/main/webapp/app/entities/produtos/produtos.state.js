(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('produtos', {
            parent: 'entity',
            url: '/produtos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.produtos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produtos/produtos.html',
                    controller: 'ProdutosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produtos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('produtos-detail', {
            parent: 'entity',
            url: '/produtos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.produtos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produtos/produtos-detail.html',
                    controller: 'ProdutosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produtos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Produtos', function($stateParams, Produtos) {
                    return Produtos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('produtos.new', {
            parent: 'produtos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produtos/produtos-dialog.html',
                    controller: 'ProdutosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                codigoEAN: null,
                                qtdeAtual: null,
                                observacoes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('produtos', null, { reload: true });
                }, function() {
                    $state.go('produtos');
                });
            }]
        })
        .state('produtos.edit', {
            parent: 'produtos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produtos/produtos-dialog.html',
                    controller: 'ProdutosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Produtos', function(Produtos) {
                            return Produtos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produtos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('produtos.delete', {
            parent: 'produtos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produtos/produtos-delete-dialog.html',
                    controller: 'ProdutosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Produtos', function(Produtos) {
                            return Produtos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produtos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
