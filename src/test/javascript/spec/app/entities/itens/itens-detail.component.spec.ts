/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { ItensDetailComponent } from '../../../../../../main/webapp/app/entities/itens/itens-detail.component';
import { ItensService } from '../../../../../../main/webapp/app/entities/itens/itens.service';
import { Itens } from '../../../../../../main/webapp/app/entities/itens/itens.model';

describe('Component Tests', () => {

    describe('Itens Management Detail Component', () => {
        let comp: ItensDetailComponent;
        let fixture: ComponentFixture<ItensDetailComponent>;
        let service: ItensService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [ItensDetailComponent],
                providers: [
                    ItensService
                ]
            })
            .overrideTemplate(ItensDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItensDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItensService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Itens(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.itens).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
