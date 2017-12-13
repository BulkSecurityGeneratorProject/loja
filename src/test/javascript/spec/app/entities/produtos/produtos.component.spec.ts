/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { Headers } from '@angular/http';

import { LojaTestModule } from '../../../test.module';
import { ProdutosComponent } from '../../../../../../main/webapp/app/entities/produtos/produtos.component';
import { ProdutosService } from '../../../../../../main/webapp/app/entities/produtos/produtos.service';
import { Produtos } from '../../../../../../main/webapp/app/entities/produtos/produtos.model';

describe('Component Tests', () => {

    describe('Produtos Management Component', () => {
        let comp: ProdutosComponent;
        let fixture: ComponentFixture<ProdutosComponent>;
        let service: ProdutosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ProdutosComponent],
                providers: [
                    ProdutosService
                ]
            })
            .overrideTemplate(ProdutosComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProdutosComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProdutosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Produtos(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.produtos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
