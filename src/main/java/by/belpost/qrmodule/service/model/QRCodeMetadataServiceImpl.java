package by.belpost.qrmodule.service.model;

import by.belpost.qrmodule.model.QRCodeMetadata;
import by.belpost.qrmodule.repository.QRCodeMetadataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class QRCodeMetadataServiceImpl implements QRCodeMetadataService {

    @Autowired
    private QRCodeMetadataRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveMetadata(String content, String path, String format,  String fileName) {
        QRCodeMetadata meta = new QRCodeMetadata();
        meta.setContent(content);
        meta.setPath(path);
        meta.setFormat(format);
        meta.setFileName(fileName);
        meta.setCreatedAt(LocalDateTime.now().withNano(0));
        repository.save(meta);
    }

    @Override
    public List<QRCodeMetadata> search(String template,
                                       Integer parcelId,
                                       String path,
                                       LocalDate createdAt,
                                       String format) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<QRCodeMetadata> cq = cb.createQuery(QRCodeMetadata.class);
        Root<QRCodeMetadata> root = cq.from(QRCodeMetadata.class);

        List<Predicate> predicates = new ArrayList<>();

        if (template != null && !template.isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("template")), template.toLowerCase()));
        }

        if (parcelId != null) {
            predicates.add(cb.equal(root.get("parcelId"), parcelId));
        }

        if (path != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("path"), path.toLowerCase()));
        }

        if (createdAt != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdAt.atTime(23, 59, 59)));
        }

        if (format != null && !format.isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("format")), format.toLowerCase()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}