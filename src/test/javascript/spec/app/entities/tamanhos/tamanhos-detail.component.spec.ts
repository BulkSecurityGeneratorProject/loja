/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { TamanhosDetailComponent } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos-detail.component';
import { TamanhosService } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.service';
import { Tamanhos } from '../../../../../../main/webapp/app/entities/tamanhos/tamanhos.model';

describe('Component Tests', () => {

    describe('Tamanhos Management Detail Component', () => {
        let comp: TamanhosDetailComponent;
        let fixture: ComponentFixture<TamanhosDetailComponent>;
        let service: TamanhosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [TamanhosDetailComponent],
                providers: [
                    TamanhosService
                ]
            })
            .overrideTemplate(TamanhosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TamanhosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TamanhosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Tamanhos(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tamanhos).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
