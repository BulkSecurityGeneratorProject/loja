/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';

import { LojaTestModule } from '../../../test.module';
import { CategoriasDetailComponent } from '../../../../../../main/webapp/app/entities/categorias/categorias-detail.component';
import { CategoriasService } from '../../../../../../main/webapp/app/entities/categorias/categorias.service';
import { Categorias } from '../../../../../../main/webapp/app/entities/categorias/categorias.model';

describe('Component Tests', () => {

    describe('Categorias Management Detail Component', () => {
        let comp: CategoriasDetailComponent;
        let fixture: ComponentFixture<CategoriasDetailComponent>;
        let service: CategoriasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LojaTestModule],
                declarations: [CategoriasDetailComponent],
                providers: [
                    CategoriasService
                ]
            })
            .overrideTemplate(CategoriasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategoriasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategoriasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Categorias(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.categorias).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
