(function() {
    'use strict';

    angular
        .module('lojaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('grade-produtos', {
            parent: 'entity',
            url: '/grade-produtos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.gradeProdutos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/grade-produtos/grade-produtos.html',
                    controller: 'GradeProdutosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gradeProdutos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('grade-produtos-detail', {
            parent: 'entity',
            url: '/grade-produtos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lojaApp.gradeProdutos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/grade-produtos/grade-produtos-detail.html',
                    controller: 'GradeProdutosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gradeProdutos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GradeProdutos', function($stateParams, GradeProdutos) {
                    return GradeProdutos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('grade-produtos.new', {
            parent: 'grade-produtos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade-produtos/grade-produtos-dialog.html',
                    controller: 'GradeProdutosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('grade-produtos', null, { reload: true });
                }, function() {
                    $state.go('grade-produtos');
                });
            }]
        })
        .state('grade-produtos.edit', {
            parent: 'grade-produtos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade-produtos/grade-produtos-dialog.html',
                    controller: 'GradeProdutosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GradeProdutos', function(GradeProdutos) {
                            return GradeProdutos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('grade-produtos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('grade-produtos.delete', {
            parent: 'grade-produtos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade-produtos/grade-produtos-delete-dialog.html',
                    controller: 'GradeProdutosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GradeProdutos', function(GradeProdutos) {
                            return GradeProdutos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('grade-produtos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
