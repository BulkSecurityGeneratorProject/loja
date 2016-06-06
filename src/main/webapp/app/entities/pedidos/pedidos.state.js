(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pedidos', {
            parent: 'entity',
            url: '/pedidos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.pedidos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedidos/pedidos.html',
                    controller: 'PedidosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pedidos');
                    $translatePartialLoader.addPart('tipoPedido');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pedidos-detail', {
            parent: 'entity',
            url: '/pedidos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.pedidos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedidos/pedidos-detail.html',
                    controller: 'PedidosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pedidos');
                    $translatePartialLoader.addPart('tipoPedido');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pedidos', function($stateParams, Pedidos) {
                    return Pedidos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('pedidos.new', {
            parent: 'pedidos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedidos/pedidos-dialog.html',
                    controller: 'PedidosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataPedido: null,
                                tipoPedido: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pedidos', null, { reload: true });
                }, function() {
                    $state.go('pedidos');
                });
            }]
        })
        .state('pedidos.edit', {
            parent: 'pedidos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedidos/pedidos-dialog.html',
                    controller: 'PedidosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pedidos', function(Pedidos) {
                            return Pedidos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedidos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pedidos.delete', {
            parent: 'pedidos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedidos/pedidos-delete-dialog.html',
                    controller: 'PedidosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pedidos', function(Pedidos) {
                            return Pedidos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedidos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
