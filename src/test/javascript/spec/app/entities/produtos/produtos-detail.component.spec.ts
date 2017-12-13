/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { ProdutosDetailComponent } from '../../../../../../main/webapp/app/entities/produtos/produtos-detail.component';
import { ProdutosService } from '../../../../../../main/webapp/app/entities/produtos/produtos.service';
import { Produtos } from '../../../../../../main/webapp/app/entities/produtos/produtos.model';

describe('Component Tests', () => {

    describe('Produtos Management Detail Component', () => {
        let comp: ProdutosDetailComponent;
        let fixture: ComponentFixture<ProdutosDetailComponent>;
        let service: ProdutosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ProdutosDetailComponent],
                providers: [
                    ProdutosService
                ]
            })
            .overrideTemplate(ProdutosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProdutosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProdutosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Produtos(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.produtos).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
